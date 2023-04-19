import asyncio
import json
import re
from typing import Dict, List, Tuple

import httpx
from fastapi import FastAPI, Request, responses, Form
from starlette import status
from starlette.responses import HTMLResponse, RedirectResponse
from starlette.templating import Jinja2Templates


app = FastAPI()
templates = Jinja2Templates(directory="templates")

CURRENCIES_URL = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies.min.json"
URL1 = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1"
URL1_DOC = "https://github.com/fawazahmed0/currency-api#readme"
URL2 = "https://api.exchangerate.host"
URL2_DOC = "https://exchangerate.host/#/"

EARLIEST_YEAR = 1999
"""Earliest possible year from URL2, URL1 stores data from max 1 year ago + some dates are missing."""

logged_in = False


# uvicorn server:app --reload
@app.get("/", response_class=HTMLResponse)
async def root(request: Request):
    return templates.TemplateResponse("login_form.html", {"request": request})


@app.post("/login/")
async def login_form(request: Request, username: str = Form(...), password: str = Form(...)):
    global logged_in
    with open("users.json") as users_file:
        users = json.loads(users_file.read())
    if password == users.get(username):
        logged_in = True
        return responses.RedirectResponse("/form", status_code=status.HTTP_303_SEE_OTHER)
    else:
        return templates.TemplateResponse("error_page.html", {"status_code": 401,
                                                              "reason_phrase": "Unauthorized",
                                                              "additional_note": "wrong credentials",
                                                              "request": request})


@app.get("/form", response_class=HTMLResponse)
async def form(request: Request):
    if logged_in:
        return templates.TemplateResponse("form.html", {"request": request})
    else:
        return templates.TemplateResponse("error_page.html", {"status_code": 401,
                                                              "reason_phrase": "Unauthorized",
                                                              "additional_note": "log in first",
                                                              "request": request})


async def request(client, url: str) -> str:
    """Collects data from given url."""
    response = await client.get(url)
    if response.is_error:
        return str({"status_code": response.status_code,
                    "reason_phrase": response.reason_phrase,
                    "additional_note": url}).replace("'", '"')
    return response.text


async def get_possible_currencies() -> Tuple:
    async with httpx.AsyncClient() as client:
        task = request(client, CURRENCIES_URL)
        result = await asyncio.gather(task)
        return result


def get_bad_request_error_dict(additional_note: str, fix: str, request: Request) -> Dict:
    return {
        "status_code": 400,
        "reason_phrase": "Bad Request",
        "additional_note": additional_note,
        "fix": fix,
        "request": request
    }


def validate_date(date: str, request: Request) -> Dict:
    """Validates date formatting and value.
    :param date: string containing date
    :return: dict with values"""
    match = re.search(r"^([0-9]){4}-", date)
    if not match:
        return get_bad_request_error_dict("unsupported date format",
                                          "Supported date format: YYYY-MM-DD.",
                                          request)

    if int(match.group()[:-1]) < EARLIEST_YEAR:
        return get_bad_request_error_dict("date is too early",
                                          f"The earliest supported year is {EARLIEST_YEAR}.",
                                          request)
    return {}


async def validate_currency(possible_currencies: Dict, currency: str, request: Request) -> Dict:
    """Checks if currency exists."""
    if currency not in possible_currencies:
        return get_bad_request_error_dict(f"currency {currency} not found",
                                          "Try again with an existing currency.",
                                          request)
    return {}


async def task(currency1: str, currency2: str, date: str) -> Tuple:
    """Generates queries and collects responses."""
    async with httpx.AsyncClient() as client:
        tasks = [request(client, f"{URL1}/{date}/currencies/{currency1}.json"),
                 request(client, f"{URL1}/{date}/currencies/{currency2}.json"),
                 request(client, f"{URL2}/convert?from={currency1}&to={currency2}&date={date}"),
                 request(client, f"{URL2}/convert?from={currency2}&to={currency1}&date={date}")]
        result = await asyncio.gather(*tasks)
        return result


async def get_currencies_data(currency1: str, currency2: str, date: str, request: Request) -> List[Dict]:
    """Collects data from responses and parses it to dictionaries."""
    results = await task(currency1, currency2, date)
    currency_info = []
    error_dict = {}
    success = False
    for result in results:
        result_dict = json.loads(result)
        if result_dict.get("status_code"):
            if not success:
                error_dict = result_dict
            currency_info.append({})
        else:
            currency_info.append(result_dict)
            success = True
            error_dict = {}

    if error_dict:
        error_dict["request"] = request
        return [error_dict]
    return currency_info


def parse_values_to_dict(values: List[Dict], currency1: str, currency2: str) -> Dict:
    """Extracts currencies' data and calculates mean value."""
    url1 = [float(values[0].get(currency1).get(currency2)) if currency1 in values[0] else -1,
            float(values[1].get(currency2).get(currency1)) if currency2 in values[1] else -1]
    url2 = [float(values[2].get("result")) if "result" in values[2] else -1,
            float(values[3].get("result")) if "result" in values[2] else -1]
    return {"URL1": url1,
            "URL2": url2,
            "mean": [round((url1[0] + url2[0]) / 2, 6) if url1[0] > -1 else url2[0],
                     round((url1[1] + url2[1]) / 2, 6) if url1[1] > -1 else url2[1]]}


async def process_data(currency_info: list, currency1: str, currency2: str) -> Dict:
    """Gathers currencies' data to final dictionary and calculates minimum and maximum."""
    result = parse_values_to_dict(currency_info[:4], currency1, currency2)
    result["mean_1_year_earlier"] = parse_values_to_dict(currency_info[4:8], currency1, currency2)["mean"]
    result["mean_10_years_earlier"] = parse_values_to_dict(currency_info[8:12], currency1, currency2)["mean"]
    result["highest"] = [max(result["mean"][0], result["mean_1_year_earlier"][0], result["mean_10_years_earlier"][0]),
                         max(result["mean"][1], result["mean_1_year_earlier"][1], result["mean_10_years_earlier"][1])]
    lowest = [result["highest"][0], result["highest"][1]]
    for index in range(2):
        for value in (result["mean"][index], result["mean_1_year_earlier"][index], result["mean_10_years_earlier"][index]):
            if value > -1 and value < lowest[index]:
                lowest[index] = value
    result["lowest"] = lowest
    return result


@app.get("/submit", response_class=HTMLResponse)
async def get_result(request: Request, currency1: str, currency2: str, date: str):
    if not logged_in:
        return templates.TemplateResponse("error_page.html", {"status_code": 401,
                                                              "reason_phrase": "Unauthorized",
                                                              "additional_note": "log in first",
                                                              "request": request})

    # check date
    if error_dict := validate_date(date, request):
        return templates.TemplateResponse("error_page.html", error_dict)

    # check currencies
    currencies = await get_possible_currencies()
    currencies = json.loads(currencies[0])
    if currencies.get("status_code"):
        error_dict = currencies
        error_dict["request"] = request
        return templates.TemplateResponse("error_page.html", error_dict)

    if (error_dict := await validate_currency(currencies, currency1, request)) \
            or (error_dict := await validate_currency(currencies, currency2, request)):
        return templates.TemplateResponse("error_page.html", error_dict)

    # current data
    currency_info = await get_currencies_data(currency1, currency2, date, request)
    if currency_info[0].get("status_code"):
        return templates.TemplateResponse("error_page.html", currency_info[0])

    # 1 year and 10 years earlier
    for date_prev in (str(int(date[:4]) - 1) + date[4:], str(int(date[:4]) - 10) + date[4:]):
        currency_info_prev = await get_currencies_data(currency1, currency2, date_prev, request)
        if len(currency_info_prev) == 4:
            currency_info.extend(currency_info_prev)
        else:
            currency_info.extend([{}, {}, {}, {}])

    return templates.TemplateResponse("results.html", {"request": request,
                                                       "c1": currency1.upper(),
                                                       "c2": currency2.upper(),
                                                       "result": await process_data(currency_info, currency1, currency2),
                                                       "url1": URL1_DOC,
                                                       "url2": URL2_DOC})



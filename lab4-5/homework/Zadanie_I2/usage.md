# Example of usage of this project:

## Compilation to `gen` directory:
```shell
../../gRPC/protoc.exe -I . --java_out=gen --plugin=protoc-gen-grpc-java=../../gRPC/protoc-gen-grpc-java-1.54.0-windows-x86_64.exe --grpc-java_out=gen calculator.proto
```

## Listing services:
```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -plaintext 127.0.0.5:50051 list
```

## Example RPC

### Calculator

Operation `12 + 24`

in commandline:
```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d "{\"arg1\": 12, \"arg2\": 24}" -plaintext 127.0.0.5:50051 calculator.Calculator/Add
```
in terminal:
```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"arg1\": 12, \"arg2\": 24}' -plaintext 127.0.0.5:50051 calculator.Calculator/Add
```

Further commands are written in the second mode, accepted by terminal.

Formatted arguments:
```json
{
  "arg1": 12,
  "arg2": 24
}
```

Result:
```json
{
  "res": 36
}
```

### AdvancedCalculator

#### Operation `24 + 21 + 1`

ComplexOperation:

```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"SUM\", \"args\": [24, 21, 1]}' -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/ComplexOperation
```

Formatted input:
```json
{
  "optype": "SUM", 
  "args": [24, 21, 1]
}
```

Result:
```json
{          
  "res": 46
} 
```

#### Operation `(2 + 2) * (1 + 1)`
BracketedComplexOperation:
```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"MULTIPLY\", \"complexArg1\": {\"optype\": \"SUM\", \"args\": [2, 2]}, \"complexArg2\": {\"optype\": \"SUM\", \"args\": [1, 1]}}' -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/BracketedComplexOperation  
```

Formatted input:
```json
{
  "optype": "MULTIPLY", 
  "complexArg1": {
    "optype": "SUM", 
    "args": [2, 2]
  }, 
  "complexArg2": {
    "optype": "SUM", 
    "args": [1, 1]
  }
}
```

result:
```json
{
  "res": 8
}
```

#### Operation `((2 + 2) * (1 + 1)) * (2 * 101)`

Expected result is `((2 + 2) * (1 + 1)) * (2 * 101) = 8 * 202 = 1616`.

```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"MULTIPLY\",\"bracketedArg1\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2, 2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [1, 1]}},\"bracketedArg2\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [101]}}}' -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/BracketedComplexOperation
```

Formatted input:
```json
{
  "optype": "MULTIPLY",
  "bracketedArg1": {
    "optype": "MULTIPLY",
    "complexArg1": {
      "optype": "SUM",
      "args": [2, 2]
    },
    "complexArg2": {
      "optype": "SUM",
      "args": [1, 1]
    }
  },
  "bracketedArg2": {
    "optype": "MULTIPLY",
    "complexArg1": {
      "optype": "SUM",
      "args": [2]
    },
    "complexArg2": {
      "optype": "SUM",
      "args": [101]
    }
  }
}
```

Result:
```json
{
  "res": 1616
}
```

#### Operation `((2 + 2) * (1 + 1)) * ((2 * 101) * (23 * (7 + 2)))`

Expected result is `((2 + 2) * (1 + 1)) * ((2 * 101) * (23 * (7 + 2))) = 8 * (202 * 138) = 334512`
```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"MULTIPLY\",\"bracketedArg1\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2, 2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [1, 1]}},\"bracketedArg2\": {\"optype\": \"MULTIPLY\",\"bracketedArg1\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [101]}},\"bracketedArg2\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [23]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [7, 2]}}}}'  -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/BracketedComplexOperation
```

Formatted input:
```json
{
  "optype": "MULTIPLY",
  "bracketedArg1": {
    "optype": "MULTIPLY",
    "complexArg1": {
      "optype": "SUM",
      "args": [2, 2]
    },
    "complexArg2": {
      "optype": "SUM",
      "args": [1, 1]
    }
  },
  "bracketedArg2": {
    "optype": "MULTIPLY",
    "bracketedArg1": {
      "optype": "MULTIPLY",
      "complexArg1": {
        "optype": "SUM",
        "args": [2]
      },
      "complexArg2": {
        "optype": "SUM",
        "args": [101]
      }
    },
    "bracketedArg2": {
      "optype": "MULTIPLY",
      "complexArg1": {
        "optype": "SUM",
        "args": [23]
      },"complexArg2": {
        "optype": "SUM",
        "args": [7, 2]
      }
    }
  }
}
```

Result:
```json
{
  "res": 334512
}
```

#### Operation `((2 + 2) * (1 + 1)) * ((2 * 101) * (23 / (229 + 1)))`

Expected result: `((2 + 2) * (1 + 1)) * ((2 * 101) * (23 / (229 + 1))) = 8 * (202 * 0.1) = 161.6`

```shell
C:\Users\bogum\Downloads\grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"MULTIPLY\",\"bracketedArg1\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2, 2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [1, 1]}},\"bracketedArg2\": {\"optype\": \"MULTIPLY\",\"bracketedArg1\": {\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [101]}},\"bracketedArg2\": {\"optype\": \"DIVIDE\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [23]},\"complexArg2\": {\"optype\": \"SUM\",\"args\": [229, 1]}}}}' -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/BracketedComplexOperation
```

Formatted input:
```json
{
  "optype": "MULTIPLY",
  "bracketedArg1": {
    "optype": "MULTIPLY",
    "complexArg1": {
      "optype": "SUM",
      "args": [2, 2]
    },
    "complexArg2": {
      "optype": "SUM",
      "args": [1, 1]
    }
  },
  "bracketedArg2": {
    "optype": "MULTIPLY",
    "bracketedArg1": {
      "optype": "MULTIPLY",
      "complexArg1": {
        "optype": "SUM",
        "args": [2]
      },
      "complexArg2": {
        "optype": "SUM",
        "args": [101]
      }
    },
    "bracketedArg2": {
      "optype": "DIVIDE",
      "complexArg1": {
        "optype": "SUM",
        "args": [23]
      },
      "complexArg2": {
        "optype": "SUM",
        "args": [229, 1]
      }
    }
  }
}
```

Result:
```json
{
  "res": 161.60000000000002
}
```

#### Operation `((2 + 1) * (10 / 5))`

Expected result: `((2 + 1) * (10 / 5)) = 3 * 2 = 6`

```shell
grpcurl_1.8.7_windows_x86_64\grpcurl.exe -d '{\"optype\": \"MULTIPLY\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": [2, 1]},\"bracketedArg2\": {\"optype\": \"DIVIDE\",\"complexArg1\": {\"optype\": \"SUM\",\"args\": 10},\"complexArg2\": {\"optype\": \"SUM\",\"args\": 5}}}' -plaintext 127.0.0.5:50051 calculator.AdvancedCalculator/BracketedComplexOperation 
```


Formatted input:
```json
{
  "optype": "MULTIPLY",
  "complexArg1": {
    "optype": "SUM",
    "args": [
      2,
      1
    ]
  },
  "bracketedArg2": {
    "optype": "DIVIDE",
    "complexArg1": {
      "optype": "SUM",
      "args": 10
    },
    "complexArg2": {
      "optype": "SUM",
      "args": 5
    }
  }
}
```

Result:
```json
{
  "res": 6
}
```

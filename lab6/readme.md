Zadanie polega na implementacji, z użyciem RabbitMQ, systemu pośredniczącego pomiędzy agencjami kosmicznymi (Agencja), a dostawcami usług transportu kosmicznego (Przewoźnik). Agencje kosmiczne zlecają wykonanie trzech typów usług: przewóz osób, przewóz ładunku, umieszczenie satelity na orbicie.

W związku z podpisanym porozumieniem, obowiązują następujące zasady współpracy:
- każdy Przewoźnik świadczy dokładnie 2 z 3 typów usług - przystępując do współpracy określa które 2 typy usług świadczy
- konkretne zlecenie na wykonanie danej usługi powinno trafić do pierwszego wolnego Przewoźnika, który obsługuje ten typ zlecenia
- dane zlecenie nie może trafić do więcej niż jednego Przewoźnika
- zlecenia identyfikowane są przez nazwę Agencji oraz wewnętrzny numer zlecenia nadawany przez Agencję
- po wykonaniu usługi Przewoźnik wysyła potwierdzenie do Agencji

W wersji premium tworzonego systemu dostępny jest dodatkowy moduł administracyjny. Administrator dostaje kopię wszystkich wiadomości przesyłanych w systemie oraz ma możliwość wysłania wiadomości w trzech trybach:
- do wszystkich Agencji
- do wszystkich Przewoźników
- do wszystkich Agencji oraz Przewoźników
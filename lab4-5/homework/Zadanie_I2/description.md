# Zadanie I2. Wywołanie dynamiczne

Celem zadania jest demonstracja działania wywołania dynamicznego po stronie klienta middleware. Wywołanie dynamiczne to takie, w którym nie jest wymagana znajomość interfejsu zdalnego obiektu lub usługi w czasie kompilacji, lecz jedynie w czasie wykonania. Wywołania powinny być zrealizowane dla kilku (trzech) różnych operacji/procedur używających przynajmniej w jednym przypadku nietrywialnych struktur danych. Nie trzeba tworzyć żadnego formatu opisującego żądanie użytkownika ani parsera jego żądań - wystarczy zawrzeć to wywołanie "na sztywno" w kodzie źródłowym, co najwyżej z konsoli parametryzując szczegóły danych. Jako bazę można wykorzystać projekt z zajęć. Warto przemyśleć przydatność takiego podejścia w budowie aplikacji rozproszonych.

ICE: Dynamic Invocation https://doc.zeroc.com/ice/3.7/client-server-features/dynamic-ice/dynamic-invocation-and-dispatch

gRPC: „dynamic grpc”, “reflection”, grpcurl

**Technologia middleware:** Ice albo gRPC

**Języki programowania:** dwa różne (jeden dla klienta, drugi dla serwera)

**Maksymalna punktacja:** Ice: 6, gRPC: 7

&ast; W przypadku użycia gRPCurl jeden język - tylko implementacja strony serwerowej.

Compilation to `gen` directory:

    ../../gRPC/protoc.exe -I . --java_out=gen --plugin=protoc-gen-grpc-java=../../gRPC/protoc-gen-grpc-java-1.54.0-windows-x86_64.exe --grpc-java_out=gen calculator.proto 


Example RPC:

    grpcurl.exe -d "{\"arg1\": 12, \"arg2\": 24}" -plaintext 127.0.0.5:50051 calculator.Calculator/Add

gives result:
```json
{
  "res": 36
}
```
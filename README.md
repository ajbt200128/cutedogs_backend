# Cutedogs Backend
Java and spark java back end
# Generate keys

To generate keys for yourself run this in the root folder
```
openssl genrsa -out private_key.pem 2048
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt
openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der
```
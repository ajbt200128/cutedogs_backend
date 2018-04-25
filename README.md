# Cutedogs Backend
Java and spark java back end
#Generate keys
To generate keys for yourself run this in the root folder

```
openssl rsa -in key.pem -pubout -out pubkey.pem
openssl rsa -outform der -in private_key.pem -out private_key.der
openssl rsa -outform der -in private_key.pem -pubout -out public_key.der
```

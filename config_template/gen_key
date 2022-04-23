#! /bin/bash
if [[ -z $1 ]] ; then
  echo "Please imput file pem"
  exit 1
fi

openssl pkcs12 -export -out eneCert.pkcs12 -in ${1}

keytool -importkeystore -srckeystore eneCert.pkcs12 -srcstoretype pkcs12 -destkeystore MyDSKeyStore.jks

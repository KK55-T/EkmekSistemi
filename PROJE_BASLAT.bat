#!/bin/bash
echo "Proje derleniyor, lutfen bekleyin..."
javac -cp .:mysql-connector-j-8.0.33.jar *.java
echo "Derleme tamamlandi. Program baslatiliyor..."
java -cp .:mysql-connector-j-8.0.33.jar Main
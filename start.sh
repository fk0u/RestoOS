#!/bin/bash

# RestoOS Launcher - Clean Environment
# Menjalankan aplikasi dengan environment yang bersih dari konflik snap

cd "$(dirname "$0")"

# Setup Java environment
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Clean up snap-related environment variables
for var in $(env | grep -i snap | cut -d= -f1); do
    unset $var
done

# Clean library paths
unset LD_LIBRARY_PATH
unset LD_PRELOAD

MVN_BIN="apache-maven-3.9.6/bin"

echo "======================================"
echo "    RestoOS - Restaurant POS System   "
echo "======================================"
echo ""
echo "Starting application..."
echo ""

if [ ! -d "$MVN_BIN" ]; then
    if command -v mvn &> /dev/null; then
        MVN_CMD="mvn"
    else
        echo "Error: Maven not found!"
        echo "Please install Maven: sudo apt install maven"
        exit 1
    fi
else
    MVN_CMD="$MVN_BIN/mvn"
fi

# Run the application
$MVN_CMD exec:java -Dexec.mainClass="com.kiloux.restopos.Main"

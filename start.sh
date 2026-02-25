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

ANT_BIN="apache-ant-1.10.14/bin"

echo "======================================"
echo "    RestoOS - Restaurant POS System   "
echo "======================================"
echo ""
echo "Starting application..."
echo ""

if [ ! -d "$ANT_BIN" ]; then
    if command -v ant &> /dev/null; then
        ANT_CMD="ant"
    else
        echo "Error: Ant not found!"
        echo "Please install Ant: sudo apt install ant"
        exit 1
    fi
else
    ANT_CMD="$ANT_BIN/ant"
fi

# Run the application
$ANT_CMD run

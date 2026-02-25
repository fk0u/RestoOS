#!/bin/bash

# Fix Java environment to avoid snap conflicts
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
unset LD_LIBRARY_PATH
unset LD_PRELOAD

MVN_BIN="apache-maven-3.9.6/bin"
ANT_BIN="apache-ant-1.10.14/bin"

echo "Checking for Ant..."
if [ -d "$ANT_BIN" ]; then
    ANT_CMD="$ANT_BIN/ant"
elif command -v ant &> /dev/null; then
    ANT_CMD="ant"
else
    echo "Ant not found. Please install Ant first:"
    echo "  sudo apt update"
    echo "  sudo apt install ant"
    exit 1
fi

echo "Building Project..."
$ANT_CMD clean copy-resources

if [ $? -ne 0 ]; then
    echo "Build Failed!"
    exit 1
fi

echo "Starting Application..."
$ANT_CMD run

#!/bin/bash
# This script is used for deleting the solutions of the vulnerabilities
set -euo pipefail
find ./backend/src/ -name '*Fixed.java' | xargs -I {} rm -f {}
find ./backend/src/ -name '*-fixed.xml' | xargs -I {} rm -f {}
find ./frontend/ -name '*.fixed' | xargs -I {} rm -f {}

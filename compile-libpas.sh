#!/bin/bash

rm -f libpas2100.{so,a,o}

gcc -m32 -O3 -Wall -Wextra -c -o libpas2100.o bin/libpas2100.c
ar q libpas2100.a libpas2100.o

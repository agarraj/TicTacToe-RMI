#!/bin/bash

javac *.java

rmic sysImplementation

rmiregistry &

#!/bin/bash
# Takes two parameters, the 'from' and 'to' versions

FROMVER=$1
TOVER=$2

perl -pi -e "s/$FROMVER/$TOVER/g" `find . -name pom.xml`


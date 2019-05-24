#!/bin/bash

# download a flattened list of zip files into ./zips ; newer files only
# -m : mirror web structure
# -np : do not climb up the directory tree
# -nH : do not create subdirectory for host
wget -P ./zips -m -np -nH  https://2019.elections.openknowledge.be/test/

# expand all those zip files in ./data and update only newer files
# -u update only newer files
# -o overwrite silently
find ./zips -iname '*.zip' -exec unzip -u -o -d ./data {} \;


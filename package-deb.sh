#!/bin/bash

#
# Copyright 2020 Austin Lehman
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# I could only get this working using the OpenJDK build avaiable
# from AdoptOpenJDK. https://adoptopenjdk.net
# The one in the standard deb repo creates a
# java.lang.module.FindException: Hash of ... differs to expected hash ... recorded in java.base

echo "Running the packager for ambient-weather-logger."
echo "Copyright 2020 Austin Lehman"
echo "Licensed under the Apache License, Version 2.0"
echo ""
echo "If all goes well resulting .deb will be in the target/ directory."
echo ""

if [ -z "$JAVA_HOME" ]
then
  echo "Error, \$JAVA_HOME is not set. Please set it in your environment or at the top of this script."
else
  echo "\$JAVA_HOME found, continuing."

  echo "Packaging with maven ...";
  mvn clean package
  if [[ "$?" -ne 0 ]]
  then
    echo "Error, build failure. Cannot package without a successful build. Exiting."
    exit 1
  else
    echo "Build successful, running jpackage ..."
    jpackage \
      -t deb \
      -d target \
      -i target \
      -n "ambient-weather-logger" \
      --app-version "1.0.0" \
      --copyright "Copyright 2020 Austin Lehman" \
      --description "Ambient Weather Logger pulls data from Ambient Weather and stores it in a MySQL database." \
      --vendor "Austin Lehman" \
      --linux-deb-maintainer "cup_of_code@fastmail.com" \
      --license-file "LICENSE" \
      --main-jar "ambient-weather-logger-1.0.jar" \
      --verbose
  fi
fi
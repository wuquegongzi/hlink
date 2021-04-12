source ~/.bash_profile

LIB_DIR=`pwd`

mvn install:install-file -Dfile="${LIB_DIR}"/ojdbc6-11.2.0.3.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar
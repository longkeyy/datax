
cd dist/engine/plugins


mkdir -p reader;for i in `ls *read*jar`;do echo $i | awk -F '-' '{print "mkdir -p reader/"$1"; mv "$0" reader/"$1}'| sh ;done;
mkdir -p writer;for i in `ls *writer*jar`;do echo $i | awk -F '-' '{print "mkdir -p writer/"$1"; mv "$0" writer/"$1}'| sh ;done;
for i in ` ls reader `;do cp plugins-common-1.0.0.jar reader/$i;done;
for i in ` ls writer `;do cp plugins-common-1.0.0.jar writer/$i;done;


cd ..
ln -s ../../lib lib

java -D-Djava.ext.dirs=lib -Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl \
 -Dtables=$tableseq \
  -jar engine-1.0.0.jar /home/search/jobs/product.xml

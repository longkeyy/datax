summary:t_dp_datax_engine
Name:t_dp_datax_engine
Version: 1.0.0
Release: 1
Group: :t_dp
License: Commercial

AutoReqProv: no

BuildArchitectures: noarch

%description
datax
%{_svn_path}
%{_svn_revision}

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
/home/ads/tools/apache-ant-1.7.1/bin/ant dist

%build

%install

dos2unix ${OLDPWD}/../release/datax.py
dos2unix ${OLDPWD}/../release/config.py
dos2unix ${OLDPWD}/../release/loader.py
dos2unix ${OLDPWD}/../release/mapper.py
dos2unix ${OLDPWD}/../release/interceptor.py
dos2unix ${OLDPWD}/../release/datax2.py
dos2unix ${OLDPWD}/../release/panguinject.py
mkdir -p .%{_prefix}/datax/bin
mkdir -p .%{_prefix}/datax/conf
mkdir -p .%{_prefix}/datax/engine
mkdir -p .%{_prefix}/datax/common
mkdir -p .%{_prefix}/datax/libs
mkdir -p .%{_prefix}/datax/jobs
mkdir -p .%{_prefix}/datax/logs

cp ${OLDPWD}/../jobs/sample/*.xml .%{_prefix}/datax/jobs
cp ${OLDPWD}/../release/*.py .%{_prefix}/datax/bin/
cp ${OLDPWD}/../release/mapper.sh .%{_prefix}/datax/bin/

cp ${OLDPWD}/../conf/interceptor.conf .%{_prefix}/datax/conf
cp ${OLDPWD}/../conf/mapper.conf .%{_prefix}/datax/conf
cp -r ${OLDPWD}/../conf/*.properties .%{_prefix}/datax/conf
cp -r ${OLDPWD}/../conf/*.xml .%{_prefix}/datax/conf

cp -r ${OLDPWD}/../build/engine/*.jar .%{_prefix}/datax/engine

cp -r ${OLDPWD}/../build/common/*.jar .%{_prefix}/datax/common
cp ${OLDPWD}/../c++/build/libcommon.so .%{_prefix}/datax/common

cp -r ${OLDPWD}/../libs/commons-io-2.0.1.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/commons-lang-2.4.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/dom4j-2.0.0-ALPHA-2.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/jaxen-1.1-beta-6.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/junit-4.4.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/log4j-1.2.16.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/slf4j-api-1.4.3.jar .%{_prefix}/datax/libs
cp -r ${OLDPWD}/../libs/slf4j-log4j12-1.4.3.jar .%{_prefix}/datax/libs

%post
chmod -R 0777 .%{_prefix}/datax/jobs
chmod -R 0777 .%{_prefix}/datax/logs

%files

%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

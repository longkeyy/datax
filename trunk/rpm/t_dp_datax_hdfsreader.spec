summary:t_dp_datax_hdfsreader
Name:t_dp_datax_hdfsreader
Version: 1.0.0
Release: 1
Group: :t_dp
License: Commercial

BuildArchitectures: noarch
Requires: t_dp_datax_engine

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
mkdir -p .%{_prefix}/datax/plugins/reader/hdfsreader

cp ${OLDPWD}/../src/com/taobao/datax/plugins/reader/hdfsreader/ParamKey.java .%{_prefix}/datax/plugins/reader/hdfsreader/
cp ${OLDPWD}/../build/plugins/hdfsreader-1.0.0.jar .%{_prefix}/datax/plugins/reader/hdfsreader/
cp ${OLDPWD}/../build/plugins/plugins-common-1.0.0.jar .%{_prefix}/datax/plugins/reader/hdfsreader
cp -r ${OLDPWD}/../libs/hadoop-0.19.2-core.jar .%{_prefix}/datax/plugins/reader/hdfsreader
cp -r ${OLDPWD}/../libs/commons-logging-1.1.1.jar .%{_prefix}/datax/plugins/reader/hdfsreader
cp -r ${OLDPWD}/../libs/libhadoop.so .%{_prefix}/datax/plugins/reader/hdfsreader

%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

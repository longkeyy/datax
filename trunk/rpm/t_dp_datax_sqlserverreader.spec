summary:t_dp_datax_sqlserverreader
Name:t_dp_datax_sqlserverreader
Version:1.0.37
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
mkdir -p .%{_prefix}/datax/plugins/reader/sqlserverreader

cp ${OLDPWD}/../src/com/taobao/datax/plugins/reader/sqlserverreader/ParamKey.java .%{_prefix}/datax/plugins/reader/sqlserverreader
cp ${OLDPWD}/../build/plugins/sqlserverreader-1.0.0.jar .%{_prefix}/datax/plugins/reader/sqlserverreader
cp ${OLDPWD}/../build/plugins/plugins-common-1.0.0.jar .%{_prefix}/datax/plugins/reader/sqlserverreader
cp -r ${OLDPWD}/../libs/sqljdbc4.jar .%{_prefix}/datax/plugins/reader/sqlserverreader
cp -r ${OLDPWD}/../libs/commons-dbcp-1.4.jar .%{_prefix}/datax/plugins/reader/sqlserverreader
cp -r ${OLDPWD}/../libs/commons-pool-1.5.4.jar .%{_prefix}/datax/plugins/reader/sqlserverreader
cp -r ${OLDPWD}/../libs/commons-logging-1.1.1.jar .%{_prefix}/datax/plugins/reader/sqlserverreader



%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Mon Aug 15 2011 xiaolong
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

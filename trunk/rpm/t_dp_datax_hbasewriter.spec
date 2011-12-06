summary:t_dp_datax_hbasewriter
Name:t_dp_datax_hbasewriter
Version:1.0.5
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
mkdir -p .%{_prefix}/datax/plugins/writer/hbasewriter

cp ${OLDPWD}/../src/com/taobao/datax/plugins/writer/hbasewriter/ParamKey.java .%{_prefix}/datax/plugins/writer/hbasewriter
cp ${OLDPWD}/../build/plugins/hbasewriter-1.0.0.jar .%{_prefix}/datax/plugins/writer/hbasewriter
cp ${OLDPWD}/../build/plugins/plugins-common-1.0.0.jar .%{_prefix}/datax/plugins/writer/hbasewriter
cp -r ${OLDPWD}/../libs/hadoop-0.20.jar .%{_prefix}/datax/plugins/writer/hbasewriter
cp -r ${OLDPWD}/../libs/zookeeper-3.3.3.jar .%{_prefix}/datax/plugins/writer/hbasewriter
cp -r ${OLDPWD}/../libs/commons-logging-1.1.1.jar .%{_prefix}/datax/plugins/writer/hbasewriter
cp -r ${OLDPWD}/../libs/hbase-0.90.2.jar .%{_prefix}/datax/plugins/writer/hbasewriter

%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Thu Oct 25 2011 zhuzhuang 
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

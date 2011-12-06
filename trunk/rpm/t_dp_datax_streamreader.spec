summary:t_dp_datax_streamreader
Name:t_dp_datax_streamreader
Version:1.0.30
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
mkdir -p .%{_prefix}/datax/plugins/reader/streamreader

cp ${OLDPWD}/../src/com/taobao/datax/plugins/reader/streamreader/ParamKey.java .%{_prefix}/datax/plugins/reader/streamreader
cp ${OLDPWD}/../build/plugins/streamreader-1.0.0.jar .%{_prefix}/datax/plugins/reader/streamreader

%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Mar 25 2011 bazhen.csy
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange/

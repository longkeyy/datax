summary:t_dp_datax_oraclewriter
Name:t_dp_datax_oraclewriter
Version: 1.0.0
Release: 1
Group: :t_dp
License: Commercial

BuildArchitectures: noarch
Requires: t_dp_datax_engine
AutoReqProv: no

%description
datax
%{_svn_path}
%{_svn_revision}

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
/home/ads/tools/apache-ant-1.7.1/bin/ant oraclewriter
%build

%install
mkdir -p .%{_prefix}/datax/plugins/writer/oraclewriter

cp ${OLDPWD}/../src/com/taobao/datax/plugins/writer/oraclewriter/ParamKey.java .%{_prefix}/datax/plugins/writer/oraclewriter
cp ${OLDPWD}/../c++/build/liboraclewriter.so .%{_prefix}/datax/plugins/writer/oraclewriter
cp ${OLDPWD}/../build/plugins/oraclewriter-1.0.0.jar .%{_prefix}/datax/plugins/writer/oraclewriter
cp -r ${OLDPWD}/../libs/libiconv.so.2 .%{_prefix}/datax/plugins/writer/oraclewriter
cp -r ${OLDPWD}/../libs/libcharset.so .%{_prefix}/datax/plugins/writer/oraclewriter


%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

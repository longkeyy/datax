summary:t_dp_datax_panguwriter
Name:t_dp_datax_panguwriter
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
mkdir -p .%{_prefix}/datax/plugins/writer/panguwriter

cp ${OLDPWD}/../src/com/taobao/datax/plugins/writer/panguwriter/ParamKey.java .%{_prefix}/datax/plugins/writer/panguwriter
cp ${OLDPWD}/../build/plugins/panguwriter-1.0.0.jar .%{_prefix}/datax/plugins/writer/panguwriter
cp ${OLDPWD}/../build/plugins/plugins-common-1.0.0.jar .%{_prefix}/datax/plugins/writer/panguwriter
cp -r ${OLDPWD}/../libs/libwrapper_pangu.so .%{_prefix}/datax/plugins/writer/panguwriter
cp -r ${OLDPWD}/../libs/libwrapper_perfcounter.so .%{_prefix}/datax/plugins/writer/panguwriter
cp -r ${OLDPWD}/../libs/wrapper_common.jar .%{_prefix}/datax/plugins/writer/panguwriter
cp -r ${OLDPWD}/../libs/wrapper_pangu.jar .%{_prefix}/datax/plugins/writer/panguwriter
cp -r ${OLDPWD}/../libs/wrapper_perfcounter.jar .%{_prefix}/datax/plugins/writer/panguwriter


%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

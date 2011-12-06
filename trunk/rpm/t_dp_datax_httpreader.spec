summary:t_dp_datax_httpreader
Name:t_dp_datax_httpreader
Version:1.0.25
Release: %(echo $RELEASE)%{?dist}
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
mkdir -p .%{_prefix}/datax/plugins/reader/httpreader

cp ${OLDPWD}/../src/com/taobao/datax/plugins/reader/httpreader/ParamKey.java .%{_prefix}/datax/plugins/reader/httpreader
cp ${OLDPWD}/../build/plugins/httpreader-1.0.0.jar .%{_prefix}/datax/plugins/reader/httpreader

%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 12 2011 hejianchao.pt
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange

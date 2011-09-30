summary: DataX httpreader can read data from http
Name: t_dp_datax_httpreader
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no 
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX httpreader can read data from http


%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant httpreader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/httpreader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/httpreader-%{version}.jar

%changelog
* Fri Aug 12 2011 hejianchao.pt
- Version 1.0.0

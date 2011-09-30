summary: DataX oraclereader can read data from oracle
Name: t_dp_datax_oraclereader
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX oraclereader can read data from oracle


%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant oraclereader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/oraclereader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/oraclereader-%{version}.jar

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


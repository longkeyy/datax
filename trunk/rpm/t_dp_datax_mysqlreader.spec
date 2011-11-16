summary: DataX mysqlreader can read data from mysql
Name: t_dp_datax_mysqlreader
Version: 1.0.0
Release: 1
Group: System
License: GPL
AutoReqProv: no 
BuildArch: noarch
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX mysqlreader can read data from mysql


%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant mysqlreader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/mysqlreader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/mysqlreader-%{version}.jar

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


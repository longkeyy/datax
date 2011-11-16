summary: DataX mysql can write data to mysql
Name: t_dp_datax_mysqlwriter
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX mysql can write data to mysql

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant mysqlwriter

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/mysqlwriter-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/mysqlwriter-%{version}.jar

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0



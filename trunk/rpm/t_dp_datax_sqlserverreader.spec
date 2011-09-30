summary: DataX sqlserverreader can read data from sqlserver
Name: t_dp_datax_sqlserverreader
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX sqlserverreader can read data from sqlserver


%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant sqlserverreader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/sqlserverreader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/sqlserverreader-%{version}.jar

%changelog
* Fri Aug 20 2010 zhouxiaolong
- Version 1.0.0


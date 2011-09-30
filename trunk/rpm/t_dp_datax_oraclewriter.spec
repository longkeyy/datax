summary: DataX oraclewriter can write data to oracle 
Name: t_dp_datax_oraclewriter
Version: 1.0.0
Release: 1
Group: System
License: GPL
AutoReqProv: no
BuildArch: noarch
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX oraclewriter can write data to oracle 

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant oraclewriter

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/oraclewriter-%{version}.jar %{dataxpath}/plugins
cp ${OLDPWD}/../c++/build/liboraclewriter.so  %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/oraclewriter-%{version}.jar
%{dataxpath}/plugins/liboraclewriter.so

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


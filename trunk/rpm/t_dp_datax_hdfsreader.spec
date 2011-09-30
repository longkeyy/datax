summary: DataX hdfsreader can read data from hadoop-hdfs 
Name: t_dp_datax_hdfsreader
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no 
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX hdfsreader can read data from hadoop-hdfs 

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant hdfsreader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/hdfsreader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/hdfsreader-%{version}.jar

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


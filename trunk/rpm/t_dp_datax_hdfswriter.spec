summary: DataX hdfswriter can write data to hadoop-hdfs 
Name: t_dp_datax_hdfswriter
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no 
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX hdfswriter can write data to hadoop-hdfs 

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant hdfswriter

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/hdfswriter-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/hdfswriter-%{version}.jar 

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


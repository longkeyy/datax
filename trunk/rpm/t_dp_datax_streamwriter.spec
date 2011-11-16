summary: DataX streamwriter can write data to stdout
Name: t_dp_datax_streamwriter
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX streamwriter can write data to stream 

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant streamwriter

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/streamwriter-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/streamwriter-%{version}.jar

%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


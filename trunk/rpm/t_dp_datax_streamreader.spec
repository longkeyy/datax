summary: DataX streamreader can read data from stream
Name: t_dp_datax_streamreader
Version: 1.0.0
Release: 1
Group: System
License: GPL
BuildArch: noarch
AutoReqProv: no
Requires: t_dp_datax_engine

%define dataxpath /home/taobao/datax

%description
DataX streamreader can read data from stream


%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant streamreader

%build

%install
mkdir -p %{dataxpath}/plugins
cp ${OLDPWD}/../build/plugins/streamreader-%{version}.jar %{dataxpath}/plugins

%files
%defattr(0755,root,root)
%{dataxpath}/plugins/streamreader-%{version}.jar

%changelog
* Fri Mar 25 2011 bazhen.csy
- Version 1.0.0

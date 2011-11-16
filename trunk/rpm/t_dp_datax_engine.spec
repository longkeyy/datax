summary: engine provides core scheduler and data swap storage for DataX 
Name: t_dp_datax_engine
Version: 1.0.0
Release: 1
Group: System
License: GPL
AutoReqProv: no 
BuildArch: noarch

%define dataxpath  /home/taobao/datax

%description
DataX Engine provides core scheduler and data swap storage for DataX 

%prep
cd ${OLDPWD}/../
export LANG=zh_CN.UTF-8
ant enginedist

%build

%install
dos2unix ${OLDPWD}/../release/datax.py

mkdir -p %{dataxpath}/bin
mkdir -p %{dataxpath}/conf
mkdir -p %{dataxpath}/engine
mkdir -p %{dataxpath}/libs
mkdir -p %{dataxpath}/jobs
mkdir -p %{dataxpath}/logs

cp ${OLDPWD}/../jobs/sample/*.xml %{dataxpath}/jobs
cp ${OLDPWD}/../release/*.py %{dataxpath}/bin/
cp ${OLDPWD}/../src/com/taobao/datax/common/plugin/ParamsKey.java %{dataxpath}/conf

cp -r ${OLDPWD}/../conf/*.properties %{dataxpath}/conf
cp -r ${OLDPWD}/../conf/*.xml %{dataxpath}/conf
cp -r ${OLDPWD}/../build/engine/*.jar %{dataxpath}/engine
cp -r ${OLDPWD}/../libs/* %{dataxpath}/libs

%post

%files
%defattr(0755,root,root)
%{dataxpath}/bin
%{dataxpath}/conf
%{dataxpath}/engine
%{dataxpath}/libs
%attr(0777,root,root) %dir /home/taobao/datax/logs
%attr(0777,root,root) %dir /home/taobao/datax/jobs


%changelog
* Fri Aug 20 2010 meining 
- Version 1.0.0


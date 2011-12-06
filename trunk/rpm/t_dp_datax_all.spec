summary:t_dp_datax_all
Name:t_dp_datax_all
Version:0.0.73
Release: 1
Group: :t_dp
License: Commercial

BuildArchitectures: noarch
Requires: t_dp_datax_engine = 1.0.174-583
Requires: t_dp_datax_hdfsreader = 1.0.43-181
Requires: t_dp_datax_hdfswriter = 1.0.44-198
Requires: t_dp_datax_hbasereader = 1.0.6-12
Requires: t_dp_datax_hbasewriter = 1.0.4-7
Requires: t_dp_datax_oraclereader = 1.0.49-172
Requires: t_dp_datax_oraclewriter = 1.0.45-203
Requires: t_dp_datax_mysqlreader = 1.0.45-145
Requires: t_dp_datax_mysqlwriter = 1.0.34-101
Requires: t_dp_datax_streamreader = 1.0.30-67
Requires: t_dp_datax_streamwriter = 1.0.31-71
Requires: t_dp_datax_httpreader = 1.0.24-55
Requires: t_dp_datax_sqlserverreader = 1.0.36-78
Requires: t_dp_datax_panguwriter = 1.0.5-13

%description
datax
%{_svn_path}
%{_svn_revision}

%prep

%build

%install

%post

%files
%defattr(0755,taobao,cug-tbdp)
%{_prefix}

%changelog
* Fri Aug 20 2010 bazhen.csy
- Version 1.0.0
- svn tag address
- http://svn.simba.taobao.com/svn/DW/arch/trunk/cheetah/services/datax/tools/dataexchange/

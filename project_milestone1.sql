--Mansi Thakkar, Mike Smith 
---BetterFuture

drop table MUTUALFUND cascade constraints;

create table MUTUALFUND(
	symbol varchar2(20) not null,
	name varchar2(30),
	description varchar2(100),
	category varchar2(10),
	c_date DATE,
	constraint pk_MUTUALFUND primary key(symbol)
	);

drop table CLOSINGPRICE cascade constraints;

create table CLOSINGPRICE(
	symbol varchar2(20) not null,
	price float,
	p_date DATE not null,
	constraint pk_closingprice primary key(symbol,p_date),
	constraint fk_closingprice foreign key(symbol) references mutualfund(symbol)
	);
	

drop table CUSTOMER cascade constraints;

create table CUSTOMER(
	login varchar2(10) not null,
	name varchar2(20),
	email varchar2(30),
	address varchar2(30),
	password varchar2(10),
	balance float,
	constraint pk_customer primary key(login)
	);

drop table ADMINISTRATOR cascade constraints;

create table ADMINISTRATOR(
	login varchar2(10) not null,
	name varchar2(20),
	email varchar2(30),
	address varchar2(30),
	password varchar2(10),
	constraint pk_administrator primary key(login)
	);

drop table ALLOCATION cascade constraints;

create table ALLOCATION(
	allocation_no int not null,
	login varchar2(20),
	p_date DATE,
	constraint pk_allocation primary key(allocation_no)
	);

drop table PREFERS cascade constraints;

create table PREFERS(
	allocation_no int,
	symbol varchar2(20),
	percentage float,
	constraint pk_PREFERS primary key(allocation_no, symbol),
	constraint fk_allocation_no foreign key(allocation_no) references ALLOCATION(allocation_no),
	constraint fk_symbol foreign key(symbol) references MUTUALFUND(symbol)
	);


drop table TRXLOG cascade constraints;

create table TRXLOG(
	trans_id int not null,
	login varchar2(10),
	symbol varchar2(20),
	t_date DATE,
	action varchar2(10),
	num_shares int,
	price float,
	amount float,
	--amount -- 
	constraint pk_TRXLOG primary key(trans_id),
	constraint fk_TRXLOG_login foreign key (login) references CUSTOMER(login),
	constraint fk_TRXLOG_symbol foreign key (symbol) references MUTUALFUND(symbol)
	);

drop table OWNS cascade constraints;

create table OWNS(
	login varchar2(10) not null,
	symbol varchar2(20) not null,
	shares int,
	constraint pk_OWNS primary key(login,symbol),
	constraint fk_OWNS_login foreign key(login) references CUSTOMER(login),
	constraint fk_OWNS_symbol foreign key(symbol) references MUTUALFUND(symbol)
	);


drop table MUTUALDATE cascade constraints;

create table MUTUALDATE(
	c_date DATE not null,
	constraint pk_MUTUALDATE primary key(c_date)
	);

COMMIT;

CREATE OR REPLACE TRIGGER SellShare
AFTER UPDATE
ON TRXLOG
FOR EACH ROW
BEGIN
	UPDATE CUSTOMER
	SET balance = :NEW.num_shares * :NEW.price
WHERE login = :NEW.login;
END;
/

create or replace procedure myproc(alc int, blc float)  is
	percentage float;
	symbol varchar2(20);
	price float;
	cursor get_percentage is
		select percentage,symbol from PREFERS
		where allocation_no = alc ;
		
	begin
		open get_percentage;
		loop
			fetch get_percentage into percentage,symbol;		
			exit when get_percentage%notfound;
		
			dbms_output.put_line(percentage);
		end loop;
		close get_percentage;
	end;
	/
	
	
create or replace trigger a
    after insert or update on TRXLOG      
    for each row
    when (new.action = 'deposit')
    declare no int;
    	     mybalance float;
    begin
      	select allocation_no
	into no
	from allocation a
	where a.login = :new.login;	
	dbms_output.put_line('allocation no: ' || no);

	select balance 
	into mybalance
	from customer c
	where c.login = :new.login;
	dbms_output.put_line('balance of customer: ' || mybalance);	
    	
	myproc(no,mybalance);

	end;
    /
	
commit;
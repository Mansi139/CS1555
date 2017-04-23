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
COMMIT;	

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
COMMIT;	

drop table ADMINISTRATOR cascade constraints;

create table ADMINISTRATOR(
	login varchar2(10) not null,
	name varchar2(20),
	email varchar2(30),
	address varchar2(30),
	password varchar2(10),
	constraint pk_administrator primary key(login)
	);
COMMIT;	

drop table ALLOCATION cascade constraints;

create table ALLOCATION(
	allocation_no int not null,
	login varchar2(20),
	p_date DATE,
	constraint pk_allocation primary key(allocation_no)
	);
COMMIT;	

drop table PREFERS cascade constraints;

create table PREFERS(
	allocation_no int,
	symbol varchar2(20),
	percentage float,
	constraint pk_PREFERS primary key(allocation_no, symbol),
	constraint fk_allocation_no foreign key(allocation_no) references ALLOCATION(allocation_no),
	constraint fk_symbol foreign key(symbol) references MUTUALFUND(symbol)
	);
COMMIT;	

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
COMMIT;	

drop table OWNS cascade constraints;

create table OWNS(
	login varchar2(10) not null,
	symbol varchar2(20) not null,
	shares int,
	constraint pk_OWNS primary key(login,symbol),
	constraint fk_OWNS_login foreign key(login) references CUSTOMER(login),
	constraint fk_OWNS_symbol foreign key(symbol) references MUTUALFUND(symbol)
	);
COMMIT;	

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
 when (new.action = 'sell')
BEGIN
	UPDATE CUSTOMER
	SET balance = balance - :NEW.num_shares * :NEW.price
WHERE login = :NEW.login;
END;
/

--sell share
create or replace procedure sellShare(comingLogin in varchar2, mutualFund in varchar2, numShares in number)
is
myShares number;
mySharePrice number;
begin
	select shares INTO myShares from  OWNS where login=comingLogin and symbol = mutualFund;
	dbms_output.put_line('share' || myShares);
	select price into mySharePrice from closingPrice where symbol=mutualFund order by p_date DESC fetch first 1 row only;
	if(myShares >= numShares) then
		update OWNS set shares = myShares - numShares where login = comingLogin and symbol=mututalFund;
		update customer set balance = balance + (mySharePrice)*(numberOfShares) where login = comingLogin;
	else
	dbms_output.put_line('balance is too low');
	end if;
end;
/
commit;

--given customer login, mutual fund Symbol and number of shares , buy share
--1) find balance, check balance >= number of shares*price
--2) buy whole number of shares, get totalBalance/price of share = number of share we can buy
--3) update balance, update OWNS shares
--t. If the balance is not sufficient to buy all the shares, no share is bought
create or replace procedure buyShare( comingLogin in varchar2, mutualFund in varchar2, numberOfShares in number)
is
mybalance number;
mySharePrice number;
begin
	dbms_output.put_line('hello');
	select balance into mybalance from customer where login=comingLogin;
	select price into mySharePrice from closingPrice where symbol=mutualFund order by p_date DESC fetch first 1 row only;
	if (mybalance >= (mySharePrice)*(numberOfShares)) then
		update OWNS set shares = shares + numberOfShares where login = comingLogin;
		update customer set balance = balance - (mySharePrice)*(numberOfShares) where login = comingLogin;
	else
	dbms_output.put_line('balance is too low');
	end if;
end;
/	
commit;

--by providing its symbol and the amount to be used in the trad
--100 in balance, i specify 50, each cost 4; numOfShare = 50/4 = 12; newbalance = 100 - (12*4) = 100-48 = 52
create or replace procedure buyShare2( comingLogin in varchar2, mutualFund in varchar2, amountToBeUsed in number)
is
mybalance number;
mySharePrice number;
numShare number;
begin
	dbms_output.put_line('hello');
	select balance into mybalance from customer where login=comingLogin;
	select price into mySharePrice from closingPrice where symbol=mutualFund order by p_date DESC fetch first 1 row only;
	if(mybalance > amountToBeUsed) then
		if (mybalance >= (mySharePrice)*(amountToBeUsed/mySharePrice)) then
			update OWNS set shares = shares + (mybalance/mySharePrice) where login = comingLogin;
			update customer set balance = balance - (mySharePrice)*(amountToBeUsed/mySharePrice) where login = comingLogin;
		else
		dbms_output.put_line('balance is too low');
		end if;
	end if;
end;
/	
commit;

create or replace procedure myproc(alc int, blc float)  is
	percentage float;
	symbol varchar2(20);
	price float;
	cursor get_percentage is
		select percentage,symbol 
		from PREFERS
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
	
/**create or replace trigger conditional
	after insert or update on TRXLOG
	for each row
	declare mydate date;
	declare action a;
begin
	select p_date
	into mydate
	from allocation a
	where a.login = :new.login;
	dbms_output.put_line('date: ' || mydate);
	
	conditionalInvestment(:new.login,a);
	
	
end;
/
commit; */

create or replace view FUNDSBOUGHT as 
select symbol, category, num_shares, t_date
from TRXLOG natural join MUTUALFUND
where action = 'buy';
	
	
create or replace procedure investingMyproc(alc in int, blc in float, loginIn in varchar2, outVar OUT int )  is
	percentage float;
	mySymbol varchar2(20);
	price float;
	totalMoney float;
	mySharePrice float;
	newBalance float; 
	flag int; 
	--if flag == 0 that means we have enough money to spend in shares, flag = 1 means no BUY transaction should happn
	cursor get_percentage is
		select percentage,symbol 
		from PREFERS
		where allocation_no = alc ;
		
	begin
		open get_percentage;
		loop
			fetch get_percentage into percentage,mySymbol;	
			--here's we can start finding out how much money it's going to cost us to buy that shares 
			exit when get_percentage%notfound;
			select price into mySharePrice from closingPrice where symbol=mySymbol;  --CHANGE IT TO GET LATEST PRICE
			--customer's deposit amount = 100, percentage = 0.5 for this share, then we need to spend 50
			--customer's deposit amount 20, percentage = 0.5
			--(blc*percentage)/100 
			newBalance := (blc*percentage)/100;
			
			IF newBalance >= (mySharePrice) THEN
				flag := 0;
			ELSE 
				flag := 1;
			END IF;

			dbms_output.put_line(percentage);
		end loop;
		--close get_percentage;


/**
	IF flag <= 0 THEN
		open get_percentage;
                loop
                        fetch get_percentage into percentage,mySymbol;
                        --here's we can start finding out how much money it's going to cost us to buy that shares
                        exit when get_percentage%notfound;
                        select price into mySharePrice from closingPrice where symbol=mySymbol;  --CHANGE IT TO GET LATEST PRICE
                        --customer's deposit amount = 100, percentage = 0.5 for this share, then we need to spend 50
                        --customer's deposit amount 20, percentage = 0.5
                        --(blc*percentage)/100
                        newBalance := (blc*percentage)/100;

                        IF newBalance >= (mySharePrice) THEN
				update OWNS set shares = shares + 1  where login := loginIN;
                		update customer set balance = balance - (mySharePrice)  where login := loginIN;


                        ELSE
                        END IF;

                        dbms_output.put_line(percentage);
                end loop;
                close get_percentage;
		--update OWNS set shares = shares + numberOfShares where login = comingLogin;
        	--update customer set balance = balance - (mySharePrice)*(numberOfShares) where login = comingLogin;
		dbms_output.put_line(flag);
	else if flag >= 1 THEN
		
	END IF;
*/
	close get_percentage;

	end;

	/

commit;	


--inversting trigger
--if transaction is deposit, triggers set of buy trasaction	
--t in buying shares of all mutual funds as specified in the allocation or none.
--Investing should either result in buying shares of all mutual funds as specified in the allocation or none. 
-- anytime you have deposit in transaction log, we see if the amount we would need to buy all the shares in preference<br />
--is enough ? If yes, we would just buy shares, (update OWNS table  and customers balance table maybe)? 

create or replace trigger investing
    after insert or update on TRXLOG      
    for each row
    when (new.action = 'deposit')
   declare 
	alloNo int;
     	mybalance float;
	flag int;
	outputValue int;    	
begin
    	select allocation_no
	into alloNo
	from allocation a
	where a.login = :old.login;	
	dbms_output.put_line('allocation no: ' || alloNo);

	--not customers balance, we need the amount we are depositing
	select amount 
	into mybalance
	from TRXLOG T
	where T.login = :new.login;
	--dbms_output.put_line('balance of customer: ' || mybalance);	
	
	--pass that amount
	investingMyproc(alloNo,mybalance, :new.login, outputValue);
	dbms_output.put_line('outPutvalue: ' || outputValue);

    	
    	/**buyShare( :new.login, mutualFund in varchar2, numberOfShares in varchar2) */
	--myproc(no,mybalance);

	end;
    /	
commit;

--this should only go to closing price and find out the price of the share (LATEST PRICE) and update the balance param
--balance param is called from cursor, and the end balance will tell us how much money we need in order to buy the shares listed in PREFERS table
/** create or replace procedure investingProc( comingLogin in varchar2, getBalance in float)
is
mybalance number;
mySharePrice number;
begin
select price into mySharePrice from closingPrice where symbol=mutualFund;
if (mybalance >= (mySharePrice)*(numberOfShares)) then
	update OWNS set shares = shares + numberOfShares where login = comingLogin;
	update customer set balance = balance - (mySharePrice)*(numberOfShares) where login = comingLogin;
else
	dbms_output.put_line('balance is too low');
end if;
end;
/	 */
commit;


insert into MutualDate values (TO_DATE('04-04-2014','MM-DD-YYYY'));

insert into Customer values ('mike', 'mike','  mike@betterfuture.com','1st street','pwd',750);
insert into Customer values ('mary', 'Mary','  mary@betterfuture.com','2st street','pwd',0);
insert into Customer values ('nan', 'nan','  nan@betterfuture.com','3st street','pwd',1);
insert into Customer values ('helo', 'helo','  helo@betterfuture.com','4st street','pwd',2);

insert into ADMINISTRATOR values('admin','Administrator','admin@betterfuture.com','5th Ave, Pitt','root');

--CHECK 
insert into MutualFund values ('MM','money-market','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('RE',' real-estate ','  real estate ','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('STB',' short-term-bonds ','short term bondst','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('LTB',' long-term-bonds','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('BBS','balance-bonds-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('SRBC','social-respons-bonds-stock','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('GS',' general-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('AS',' aggressive-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into MutualFund values ('IMS',' international-markets-stock','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));

insert into Owns values('mike','RE',50);
insert into Owns values('mike','MM',10);

--insert into TRXLOG values(0,'mike','MM',TO_DATE('03-23-2014','MM-DD-YYYY'),'deposit',NULL,NULL,1000);
insert into TRXLOG values(1,'mike','MM',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);
insert into TRXLOG values(2,'mike','RE',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);
insert into TRXLOG values(3,'mike','MM',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);


insert into Allocation values (0,'mike',TO_DATE('03-28-2014','MM-DD-YYYY'));
insert into Allocation values (1,'mary',TO_DATE('03-29-2014','MM-DD-YYYY'));
insert into Allocation values (2,'mike',TO_DATE('03-03-2014','MM-DD-YYYY'));

insert into Prefers values (0,'MM',0.5);
insert into Prefers values (0,'RE',0.5);
insert into Prefers values (1,'STB',0.2);
insert into Prefers values (1,'LTB',0.4);
insert into Prefers values (1,'BBS',0.4);
insert into Prefers values (2,'GS',0.3);
insert into Prefers values (2,'AS',0.3);
insert into Prefers values (2,'IMS',0.4);

insert into ClosingPrice values ('MM',10,TO_DATE('04-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('MM',11,TO_DATE('05-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('MM',12,TO_DATE('06-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('MM',15,TO_DATE('07-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('MM',14,TO_DATE('08-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('MM',15,TO_DATE('09-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',16,TO_DATE('10-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',10,TO_DATE('11-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',12,TO_DATE('12-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',15,TO_DATE('01-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',10,TO_DATE('02-04-2014','MM-DD-YYYY'));
insert into ClosingPrice values ('RE',9,TO_DATE('03-04-2014','MM-DD-YYYY'));

commit;

--in the investment, figure out why having a IF afte end loop not accceptable?  

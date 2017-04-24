INSERT INTO MUTUALDATE(c_date) values('04-APR-14');

INSERT INTO CUSTOMER(login, name, email, address, password, balance) values('mike', 'Mike', 'mike@betterfuture.com', '1st street', 'pwd', 750);
INSERT INTO CUSTOMER(login, name, email, address, password, balance) values('mary', 'Mary', 'mary@betterfuture.com', '2st street', 'pwd', 0);
INSERT INTO CUSTOMER(login, name, email, address, password, balance) values ('nan', 'nan','  nan@betterfuture.com','3st street','pwd',1);
INSERT INTO CUSTOMER(login, name, email, address, password, balance) values ('helo', 'helo',' helo@betterfuture.com','4st street','pwd',2);



INSERT INTO ADMINISTRATOR(login, name, email, address, password) values('admin', 'Administrator', 'admin@betterfuture.com', '5th Ave, Pitt', 'root');



INSERT INTO MutualFund values ('MM','money-market','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('RE',' real-estate ','  real estate ','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('STB',' short-term-bonds ','short term bondst','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('LTB',' long-term-bonds','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('BBS','balance-bonds-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('SRBC','social-respons-bonds-stock','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('GS',' general-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('AS',' aggressive-stocks','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO MutualFund values ('IMS',' international-markets-stock','monery market','fixed',TO_DATE('04-04-2014','MM-DD-YYYY'));

INSERT INTO OWNS(login, symbol, shares) values('mike', 'RE', 50);

INSERT INTO TRXLOG values(0,'mike',NULL,TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);
INSERT INTO TRXLOG values(1,'mike','MM',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);
INSERT INTO TRXLOG values(2,'mike','RE',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);
INSERT INTO TRXLOG values(3,'mike','MM',TO_DATE('04-04-2014','MM-DD-YYYY'),'buy',NULL,NULL,1000);

INSERT INTO Allocation values (0,'mike',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT INTO Allocation values (1,'mike',TO_DATE('04-04-2014','MM-DD-YYYY'));
INSERT into Allocation values (2,'mike',TO_DATE('04-04-2014','MM-DD-YYYY'));


INSERT INTO PREFERS(allocation_no, symbol, percentage) values(0, 'MM', .5);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(0, 'RE', .5);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(1, 'STB', .2);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(1, 'LTB', .4);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(1, 'BBS', .4);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(2, 'GS', .3);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(2, 'AS', .3);
INSERT INTO PREFERS(allocation_no, symbol, percentage) values(2, 'IMS', .4);


        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 10, TO_DATE('04-28-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 11, TO_DATE('10-29-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 12, TO_DATE('09-03-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 15, TO_DATE('08-02-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 14, TO_DATE('07-01-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 15, TO_DATE('06-31-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('MM', 16, TO_DATE('04-30-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 10, TO_DATE('10-29-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 12, TO_DATE('11-28-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 15, TO_DATE('12-27-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 14, TO_DATE('04-26-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 16, TO_DATE('03-25-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 17, TO_DATE('08-24-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('RE', 15, TO_DATE('12-23-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 10, TO_DATE('02-22-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 9, TO_DATE('04-21-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 10, TO_DATE('07-20-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 12, TO_DATE('03-19-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 14, TO_DATE('02-18-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 10, TO_DATE('03-17-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('STB', 12, TO_DATE('01-16-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 10, TO_DATE('03-15-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 12, TO_DATE('05-14-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 13, TO_DATE('07-13-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 15, TO_DATE('09-12-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 12, TO_DATE('11-11-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 9, TO_DATE('10-10-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('LTB', 10, TO_DATE('02-09-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 10, TO_DATE('03-08-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 11, TO_DATE('04-07-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 14, TO_DATE('05-06-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 18, TO_DATE('06-05-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 13, TO_DATE('07-04-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 15, TO_DATE('08-03-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('BBS', 16, TO_DATE('09-02-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 10, TO_DATE('10-01-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 12, TO_DATE('11-20-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 12, TO_DATE('01-20-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 14, TO_DATE('03-10-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 17, TO_DATE('11-11-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 20, TO_DATE('11-12-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('SRBS', 20, TO_DATE('05-13-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 10, TO_DATE('03-14-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 12, TO_DATE('06-15-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 13, TO_DATE('01-16-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 15, TO_DATE('10-17-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 14, TO_DATE('03-18-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 15, TO_DATE('12-19-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('GS', 12, TO_DATE('11-20-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 10, TO_DATE('03-12-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 15, TO_DATE('05-14-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 14, TO_DATE('06-16-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 16, TO_DATE('02-18-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 14, TO_DATE('02-20-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 17, TO_DATE('11-01-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('AS', 18, TO_DATE('09-03-2014', 'MM-DD-YYYY'));
        commit;
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 10, TO_DATE('10-05-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 12, TO_DATE('07-07-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 12, TO_DATE('06-09-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 14, TO_DATE('05-11-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 13, TO_DATE('04-13-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 12, TO_DATE('02-15-2014', 'MM-DD-YYYY'));
        INSERT INTO CLOSINGPRICE(symbol, price, p_date) VALUES('IMS', 11, TO_DATE('01-17-2014', 'MM-DD-YYYY'));

commit;

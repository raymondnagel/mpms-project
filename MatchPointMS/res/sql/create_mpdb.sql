CREATE SCHEMA MATCHPOINT;

-- -----------------------------------------------------
-- Table MATCHPOINT.Player (none)
-- A player who has visited the club at least once.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.Player (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  since TIMESTAMP NOT NULL,
  password VARCHAR(40),
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40) NOT NULL,
  sex VARCHAR(1) NOT NULL,
  birth_date TIMESTAMP NOT NULL,
  club_rating INT,
  rating_acc DOUBLE,
  phone VARCHAR(10) NOT NULL,
  email VARCHAR(40),
  address VARCHAR(80),
  city VARCHAR(40),
  state VARCHAR(2),
  zip VARCHAR(5),
  sponsor_id INT,
  balance DOUBLE NOT NULL,
  visits INT NOT NULL,
  position VARCHAR(40),
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table MATCHPOINT.Lesson (Player)
-- A lesson appointment.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.Lesson (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  player_id INT NOT NULL,
  date TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id)
   REFERENCES MATCHPOINT.Player (id)
);

-- -----------------------------------------------------
-- Table MATCHPOINT.Visit (Player)
-- A player visits the club.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.Visit (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  player_id INT NOT NULL,
  date TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id)
   REFERENCES MATCHPOINT.Player (id)
);

-- -----------------------------------------------------
-- Table MATCHPOINT.TTMatch (Player)
-- Two players play a singles match.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.TTMatch (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  winner_id INT NOT NULL,
  loser_id INT NOT NULL,
  points_ex INT NOT NULL,
  date TIMESTAMP NOT NULL,
  winner_new_rating INT NOT NULL,
  loser_new_rating INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (winner_id)
   REFERENCES MATCHPOINT.Player (id),
  FOREIGN KEY (loser_id)
   REFERENCES MATCHPOINT.Player (id)
);

-- -----------------------------------------------------
-- Table MATCHPOINT.Charge (Player)
-- Increase player's balance for a purchase.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.Charge (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  player_id INT NOT NULL,
  date TIMESTAMP NOT NULL,
  amount DOUBLE NOT NULL,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id)
   REFERENCES MATCHPOINT.Player (id)
);

-- -----------------------------------------------------
-- Table MATCHPOINT.Transact (Player)
-- The balance is what the player owes me.
-- [PAYMENT] in the description means the player paid me to reduce his balance.
-- [REFUND] in the description means that I reduced the player's balance without a payment.
-- [-TRANSFER] in the description means that the player's balance was transferred to another player (sponsor).
-- [+TRANSFER] in the description means that the another player's balance was transferred to this player (sponsor).
-- + Amounts decrease the player's balance. Any - Amount must have a corresponding + Amount record for another player.
-- -----------------------------------------------------
CREATE TABLE MATCHPOINT.Transact (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  player_id INT NOT NULL,
  date TIMESTAMP NOT NULL,
  amount DOUBLE NOT NULL,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (player_id)
   REFERENCES MATCHPOINT.Player (id)
);
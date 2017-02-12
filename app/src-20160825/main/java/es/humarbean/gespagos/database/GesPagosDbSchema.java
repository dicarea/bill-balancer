package es.humarbean.gespagos.database;

public class GesPagosDbSchema {
    public static final class MateTable {
        public static final String NAME = "mate";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String NUM_PAYMENTS = "numPayments";
            public static final String NUM_ROUNDS = "numRounds";
        }
    }

    public static final class RoundTable {
        public static final String NAME = "round";

        public static final class Cols {
            public static final String ID = "id";
            public static final String DATE = "date";
            public static final String PLACE = "place";
            public static final String DELETED = "deleted";
        }
    }

    public static final class PaymentTable {
        public static final String NAME = "payment";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ID_ROUND = "idRound";
            public static final String ID_MATE = "idMate";
            public static final String NUM_ITEMS = "numItems";
            public static final String IS_PAYER = "isPayer";
        }
    }
}


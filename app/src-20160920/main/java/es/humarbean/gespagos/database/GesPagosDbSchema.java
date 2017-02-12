package es.humarbean.gespagos.database;

public class GesPagosDbSchema {
    public static final class MateTable {
        public static final String NAME = "tmate";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ID_GROUP = "idGroup";
            public static final String NAME = "name";
            public static final String SELECTED = "selected";
        }
    }

    public static final class RoundTable {
        public static final String NAME = "tround";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ID_GROUP = "idGroup";
            public static final String DATE = "date";
            public static final String PLACE = "place";
            public static final String DELETED = "deleted";
        }
    }

    public static final class PaymentTable {
        public static final String NAME = "tpayment";

        public static final class Cols {
            public static final String ID = "id";
            public static final String ID_ROUND = "idRound";
            public static final String ID_GROUP = "idGroup";
            public static final String ID_MATE = "idMate";
            public static final String NUM_ITEMS = "numItems";
            public static final String IS_PAYER = "isPayer";
        }
    }

    public static final class GroupTable {
        public static final String NAME = "tgroup";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String SELECTED = "selected";
        }
    }
}


package es.humarbean.gespagos.database.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.humarbean.gespagos.database.GesPagosDbSchema.PaymentTable;
import es.humarbean.gespagos.models.Payment;

public class PaymentCursorWrapper extends CursorWrapper {
    public PaymentCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Payment getPayment() {
        Integer id = getInt(getColumnIndex(PaymentTable.Cols.ID));
        Integer idRound = getInt(getColumnIndex(PaymentTable.Cols.ID_ROUND));
        Integer idMate = getInt(getColumnIndex(PaymentTable.Cols.ID_MATE));
        Integer numItems = getInt(getColumnIndex(PaymentTable.Cols.NUM_ITEMS));
        Integer isPayer = getInt(getColumnIndex(PaymentTable.Cols.IS_PAYER));

        Payment payment = new Payment();
        payment.setId(id);
        payment.setIdRound(idRound);
        payment.setIdMate(idMate);
        payment.setNumItems(numItems);
        payment.setPayer(isPayer > 0);

        return payment;
    }
}

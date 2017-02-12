package es.humarbean.gespagos.database.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import es.humarbean.gespagos.database.GesPagosDbSchema.RoundTable;
import es.humarbean.gespagos.models.Round;

public class RoundCursorWrapper extends CursorWrapper {
    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() {
        Integer id = getInt(getColumnIndex(RoundTable.Cols.ID));
        Integer idGroup = getInt(getColumnIndex(RoundTable.Cols.ID_GROUP));
        Long date = getLong(getColumnIndex(RoundTable.Cols.DATE));
        String place = getString(getColumnIndex(RoundTable.Cols.PLACE));

        Round round = new Round();
        round.setId(id);
        round.setIdGroup(idGroup);
        round.setDate(new Date(date));
        round.setPlace(place);

        return round;
    }
}

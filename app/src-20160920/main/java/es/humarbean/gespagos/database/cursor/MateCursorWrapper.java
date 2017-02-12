package es.humarbean.gespagos.database.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.models.Mate;

public class MateCursorWrapper extends CursorWrapper {
    public MateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Mate getMate() {
        Integer id = getInt(getColumnIndex(MateTable.Cols.ID));
        String name = getString(getColumnIndex(MateTable.Cols.NAME));
        Integer idGroup = getInt(getColumnIndex(MateTable.Cols.ID_GROUP));
        Integer selected = getInt(getColumnIndex(MateTable.Cols.SELECTED));

        Mate mate = new Mate();
        mate.setId(id);
        mate.setName(name);
        mate.setIdGroup(idGroup);
        mate.setSelected(selected != null && selected == 1);

        return mate;
    }
}

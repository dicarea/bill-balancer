package es.humarbean.gespagos.database.cursor;

import android.database.Cursor;
import android.database.CursorWrapper;

import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.models.Group;

public class GroupCursorWrapper extends CursorWrapper {
    public GroupCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Group getGroup() {
        Integer id = getInt(getColumnIndex(MateTable.Cols.ID));
        String name = getString(getColumnIndex(MateTable.Cols.NAME));
        Integer selected = getInt(getColumnIndex(MateTable.Cols.SELECTED));

        Group mate = new Group();
        mate.setId(id);
        mate.setName(name);
        mate.setSelected(selected != null && selected == 1);

        return mate;
    }
}

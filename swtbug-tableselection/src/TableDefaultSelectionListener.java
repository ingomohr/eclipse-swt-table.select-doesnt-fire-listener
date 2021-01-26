
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * SWT Bug Showcase
 * <p>
 * Run the class as Java application from within Eclipse.
 * </p>
 * <p>
 * The class opens a window with a table of 10 items which auto-changes the
 * selection each and every second.
 * </p>
 * <p>
 * <ul>
 * <li><b>Expected</b>: console prints <code>"listener fired"</code> whenever
 * the selection changes</li>
 * <li><b>Actual</b>: console prints that text only when you change the selection
 * with the mouse</li>
 * </ul>
 * </p>
 */
public class TableDefaultSelectionListener {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		for (int i = 0; i < 10; i++) {
			TableItem item = new TableItem(table, 0);
			item.setText("Item " + i);
		}
		table.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> System.out.println("listener fired")));

		shell.pack();
		shell.open();

		new Thread(() -> {
			changeSelectionEachSecond(table);
		}).start();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void changeSelectionEachSecond(Table pTable) {
		final AtomicInteger index = new AtomicInteger(1);

		while (true) {
			try {
				Thread.sleep(1_000);
			} catch (InterruptedException e) {
			}

			index.set((index.get() + 1) % 5);

			Display.getDefault().syncExec(() -> {
				int selectionIndex = index.get();
				System.out.println("selecting index : " + selectionIndex);
				pTable.deselectAll();
				pTable.select(selectionIndex);
			});
		}

	}
}
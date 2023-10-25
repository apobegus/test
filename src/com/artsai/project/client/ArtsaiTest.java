package com.artsai.project.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.AreaChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ArtsaiTest implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final DiagramServiceAsync diagramService = GWT.create(DiagramService.class);
	private final TableServiceAsync tableService = GWT.create(TableService.class);

	private static final List<String> elements = Arrays.asList("Diagrams", "Tables");

	private DockLayoutPanel holder = null;
	private TabLayoutPanel diagramTab = null;
	private TabLayoutPanel tableTab = null;
	private SimpleLayoutPanel diagram = null;
	private SimpleLayoutPanel tables = null;

	private DeckLayoutPanel container = null;

	private AreaChart ctrChart = null;
	private AreaChart evpmChart = null;

	private ListBox eventBox = null;
	private Button eventButton = null;

	private DataGrid<TableModel> dmaGrid = null;
	private DataGrid<TableModel> siteGrid = null;
	
	private PopupPanel busy = null;

	public void onModuleLoad() {
		busy = new PopupPanel();
		busy.add(new Label("Loading..."));
		busy.setWidth("200px");
		busy.addStyleName("my-Busy");

		holder = new DockLayoutPanel(Unit.EM);

		HorizontalPanel header = new HorizontalPanel();
		HorizontalPanel eventHolder = new HorizontalPanel();

		Label eventLabel = new Label("Event");
		eventBox = new ListBox();
		eventBox.setWidth("300px");
		eventButton = new Button("Load");

		eventHolder.add(eventLabel);
		eventHolder.add(eventBox);
		eventHolder.add(eventButton);
		eventHolder.setSpacing(5);
		eventHolder.setCellVerticalAlignment(eventLabel, HasAlignment.ALIGN_MIDDLE);
		eventHolder.setCellVerticalAlignment(eventBox, HasAlignment.ALIGN_MIDDLE);
		eventHolder.setCellVerticalAlignment(eventButton, HasAlignment.ALIGN_MIDDLE);

		Label caption = new Label("Artsai Test Task");

		caption.setStyleName("my-Header-Application");

		header.add(new HTML("&nbsp;"));
		header.add(caption);
		header.add(eventHolder);
		header.setWidth("100%");
		header.setCellHorizontalAlignment(eventHolder, HasAlignment.ALIGN_RIGHT);
		header.setCellVerticalAlignment(caption, HasAlignment.ALIGN_MIDDLE);

		diagram = new SimpleLayoutPanel();
		diagram.setWidth("100%");

		diagramTab = new TabLayoutPanel(2.5, Unit.EM);
		diagramTab.setWidth("100%");

		diagram.add(diagramTab);

		container = new DeckLayoutPanel();

		VerticalPanel menuPanel = new VerticalPanel();
		menuPanel.setSpacing(4);
		menuPanel.setWidth("100%");

		TextCell menuCell = new TextCell();

		CellList<String> menuList = new CellList<String>(menuCell);
		menuList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		menuList.addStyleName("my-Menu");

		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

		menuList.setSelectionModel(selectionModel);
		menuList.setRowCount(elements.size(), true);
		menuList.setRowData(0, elements);

		menuPanel.add(menuList);

		// ------------------------------------------------------------------------------------------
		
		/*
		dmaGrid = new DataGrid<TableModel>();
		dmaGrid.setLoadingIndicator(null);
		
		TextColumn<TableModel> nameColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getName();
			}
		};

		TextColumn<TableModel> impressionColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getImpression();
			}
		};

		TextColumn<TableModel> ctrColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getCtr();
			}
		};

		TextColumn<TableModel> evpmColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getEvpm();
			}
		};

		dmaGrid.addColumn(nameColumn, "MM_DMA");
		dmaGrid.addColumn(impressionColumn, "Impression");
		dmaGrid.addColumn(ctrColumn, "CTR %");
		dmaGrid.addColumn(evpmColumn, "EvPM %");
		
		dmaGrid.setColumnWidth(nameColumn, 100, Unit.PX);
		dmaGrid.setColumnWidth(impressionColumn, 200, Unit.PX);
		dmaGrid.setColumnWidth(ctrColumn, 100, Unit.PX);
	    
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    SimplePager dmaPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    dmaPager.setDisplay(dmaGrid);
	    
	    SingleSelectionModel<TableModel> selection = new SingleSelectionModel<TableModel>();
		dmaGrid.setSelectionModel(selection);
		
		DockLayoutPanel dmaPanel = new DockLayoutPanel(Unit.EM);
		dmaPanel.addSouth(dmaPager, 3);
		dmaPanel.add(dmaGrid);
		*/
				
		tableTab = new TabLayoutPanel(2.5, Unit.EM);

		dmaGrid = getDataGrid("MM_DMA");
		siteGrid = getDataGrid("SITE_ID");

		tables = new SimpleLayoutPanel();
		tables.add(tableTab);

		// ------------------------------------------------------------------------------------------

		container.add(diagram);
		container.add(tables);
		container.showWidget(diagram);

		holder.addNorth(header, 3);
		holder.addWest(menuPanel, 10);
		holder.add(container);

		loadEvents();
		loadCharts();

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				String selected = selectionModel.getSelectedObject();

				if (selected != null) {
					if (selected.equals("Diagrams")) {
						container.showWidget(diagram);
						container.forceLayout();
					}

					if (selected.equals("Tables")) {
						container.showWidget(tables);
						container.forceLayout();
					}
				}
			}
		});

		eventButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (container.getVisibleWidgetIndex() == 0) {
					busy.center();
					busy.show();

					loadDiagrams(eventBox.getSelectedItemText());
				}
				
				if (container.getVisibleWidgetIndex() == 1) {
					busy.center();
					busy.show();

					loadTables(eventBox.getSelectedItemText());
				}
			}
		});

		RootLayoutPanel.get().add(holder);
	}

	private void loadEvents() {
		greetingService.greetServer("loadEvents", new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				List<String> list = result;

				for (int i = 0; i < list.size(); i++) {
					eventBox.addItem(list.get(i));
				}
			};
		});
	}

	private void loadCharts() {
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		
		chartLoader.loadApi(new Runnable() {
			public void run() {
				ctrChart = new AreaChart();
				diagramTab.add(ctrChart, "CTR");

				evpmChart = new AreaChart();
				diagramTab.add(evpmChart, "EvPM");
			}
		});
	}

	private void loadDiagrams(String event) {
		String input = event;

		diagramService.diagramServer(input, new AsyncCallback<DiagramModel>() {
			public void onFailure(Throwable caught) {
				busy.hide();
				Window.alert(caught.getMessage());
			}

			public void onSuccess(DiagramModel result) {
				busy.hide();
				DiagramModel model = result;

				drawCTRChart(ctrChart, model);
				drawEvPMChart(evpmChart, model);

				diagramTab.forceLayout();
			}
		});
	}

	private void drawCTRChart(AreaChart chart, DiagramModel model) {
		String[] days = model.getDates().toArray(new String[0]);
		Integer[] ctr = model.getCtr().toArray(new Integer[0]);

		int[][] values = new int[1][ctr.length];

		for (int i = 0; i < ctr.length; i++) {
			values[0][i] = ctr[i].intValue();
		}

		// Prepare the data
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "Year");
		dataTable.addColumn(ColumnType.NUMBER, "CTR");

		dataTable.addRows(days.length);

		for (int i = 0; i < days.length; i++) {
			dataTable.setValue(i, 0, days[i]);
		}
		
		for (int col = 0; col < values.length; col++) {
			for (int row = 0; row < values[col].length; row++) {
				dataTable.setValue(row, col + 1, values[col][row]);
			}
		}

		// Set options
		AreaChartOptions options = AreaChartOptions.create();

		options.setTitle("CTR (click rate) = 100 * click_count / impession_count %");
		options.setFontSize(10);
		options.setColors("orchid");
		// options.setIsStacked(true);
		options.setHAxis(HAxis.create("Days"));
		options.setVAxis(VAxis.create("Value / 100"));

		chart.draw(dataTable, options);
	}

	private void drawEvPMChart(AreaChart chart, DiagramModel model) {
		String[] days = model.getDates().toArray(new String[0]);
		Integer[] evpm = model.getEvpm().toArray(new Integer[0]);

		int[][] values = new int[1][evpm.length];

		for (int i = 0; i < evpm.length; i++) {
			values[0][i] = evpm[i].intValue();
		}

		// Prepare the data
		DataTable dataTable = DataTable.create();
		
		dataTable.addColumn(ColumnType.STRING, "Year");
		dataTable.addColumn(ColumnType.NUMBER, "EvPM");
		dataTable.addRows(days.length);

		for (int i = 0; i < days.length; i++) {
			dataTable.setValue(i, 0, days[i]);
		}
		
		for (int col = 0; col < values.length; col++) {
			for (int row = 0; row < values[col].length; row++) {
				dataTable.setValue(row, col + 1, values[col][row]);
			}
		}

		// Set options
		AreaChartOptions options = AreaChartOptions.create();

		options.setTitle("EvPM (event rate) = 1000 * event_count / impression_count %");
		options.setFontSize(10);
		// options.setIsStacked(true);
		options.setHAxis(HAxis.create("Days"));
		options.setVAxis(VAxis.create("Value / 100"));

		chart.draw(dataTable, options);
	}
	
	private void loadTables(String event) {
		String input = event;

		tableService.tableServer(input, "mm_dma", new AsyncCallback<TableModel[]>() {
			public void onFailure(Throwable caught) {
				busy.hide();
				Window.alert(caught.getMessage());
			}
			
			public void onSuccess(TableModel[] result) {
				busy.hide();
				
			    ListDataProvider<TableModel> dataProvider = new ListDataProvider<TableModel>();
			    List<TableModel> data = dataProvider.getList();

				for (int i = 0; i < result.length; i++) {
					data.add(result[i]);
				}
			    
			    dataProvider.addDataDisplay(dmaGrid);
			    tableTab.forceLayout();
			}
		});

		tableService.tableServer(input, "site_id", new AsyncCallback<TableModel[]>() {
			public void onFailure(Throwable caught) {
				busy.hide();
				Window.alert(caught.getMessage());
			}
			
			public void onSuccess(TableModel[] result) {
				busy.hide();
				
			    ListDataProvider<TableModel> dataProvider = new ListDataProvider<TableModel>();
			    List<TableModel> data = dataProvider.getList();

				for (int i = 0; i < result.length; i++) {
					data.add(result[i]);
				}
			    
			    dataProvider.addDataDisplay(siteGrid);
			    tableTab.forceLayout();
			}
		});

	}

	
	private DataGrid<TableModel> getDataGrid(String name) {
		DataGrid<TableModel> grid = new DataGrid<TableModel>();
		grid.setLoadingIndicator(null);
		
		TextColumn<TableModel> nameColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getName();
			}
		};

		TextColumn<TableModel> impressionColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getImpression();
			}
		};

		TextColumn<TableModel> ctrColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getCtr();
			}
		};

		TextColumn<TableModel> evpmColumn = new TextColumn<TableModel>() {
			@Override
			public String getValue(TableModel model) {
				return model.getEvpm();
			}
		};

		grid.addColumn(nameColumn, "ID");
		grid.addColumn(impressionColumn, "Impression");
		grid.addColumn(ctrColumn, "CTR %");
		grid.addColumn(evpmColumn, "EvPM %");
		
		if (name.equals("SITE_ID")) grid.setColumnWidth(nameColumn, 500, Unit.PX);
		else grid.setColumnWidth(nameColumn, 200, Unit.PX);
		grid.setColumnWidth(impressionColumn, 200, Unit.PX);
		grid.setColumnWidth(ctrColumn, 100, Unit.PX);
	    
	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
	    SimplePager pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
	    pager.setDisplay(grid);
	    
	    SingleSelectionModel<TableModel> selection = new SingleSelectionModel<TableModel>();
		grid.setSelectionModel(selection);
		
		DockLayoutPanel panel = new DockLayoutPanel(Unit.EM);
		panel.addSouth(pager, 3);
		panel.add(grid);
		
		tableTab.add(panel, name);

		return grid;
	}
	
}

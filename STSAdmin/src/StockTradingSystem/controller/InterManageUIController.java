package StockTradingSystem.controller;

import StockTradingSystem.Main;
import StockTradingSystem.utils.CustomResp;
import StockTradingSystem.utils.HttpCommon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InterManageUIController extends AdminUIController {
    private Main application;
    @FXML private ChoiceBox<String> Choiceboxlimit;
    @FXML private ChoiceBox<String> Choiceboxstate;
    @FXML private TableView<StockProperty> stocktableview;
    @FXML private TableColumn<StockProperty,String> jfxstnametv;    //股票名称列
    @FXML private TableColumn<StockProperty,String> jfxstlimittv;    //股票涨跌幅限制列
    @FXML private TableColumn<StockProperty,String> jfxstcodetv;    //股票代码列
    @FXML private TableColumn<StockProperty,Double> jfxstceiltv;    //股票涨停价格列
    @FXML private TableColumn<StockProperty,Double> jfxstfloortv;    //股票跌停价格列
    @FXML private TableColumn<StockProperty,Double> jfxstpricetv;    //股票价格列
    @FXML private TableColumn<StockProperty,String> jfxststatetv;    //股票交易状态列
    @FXML private TableColumn<StockProperty,String> jfxstchangetv;    //股票涨跌幅（现）列
    @FXML private TableView<IndexProperty> indextableview;
    @FXML private TableColumn<IndexProperty,String> jfxinnametv;    //指数名称列
    @FXML private TableColumn<IndexProperty,String> jfxincodetv;    //指数代码列
    @FXML private TableColumn<IndexProperty,String> jfxinnumtv;    //指数数值列
    private ObservableList<StockProperty> stockObservableList = FXCollections.observableArrayList();
    private ObservableList<IndexProperty> indexObservableList = FXCollections.observableArrayList();

    public void setApp(Main app) { this.application = app; }
    public Main getApp() {return this.application; }

    public void modifyPassword() throws Exception{
        // TODO 跳到修改密码界面
        application.createChangePasswordUI();
    }

    @Override
    public void logout() throws Exception {
        application.stage.close();
        application.gotoAdminLoginUI();
    }

    @Override
    public void quit() {
        // TODO 退出
        application.stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 显示股票信息
        displaystock();
        bindstock();
        // TODO 显示指数信息，但现在还没有
        displayindex();
        bindindex();
        // TODO 下拉框初始化
        setChoicebox();
        super.initialize(url, rb);
    }

    public void setChoicebox(){
        Choiceboxstate.getItems().add("正常交易");
        Choiceboxstate.getItems().add("暂停交易");
        Choiceboxstate.getItems().add("停牌三天");
        Choiceboxstate.setValue("正常交易");
        Choiceboxlimit.getItems().add("5%");
        Choiceboxlimit.getItems().add("10%");
        Choiceboxlimit.getItems().add("无限制");
        Choiceboxlimit.setValue("10%");
    }

    public void bindstock(){
        // TODO 股票数据绑定TableView
        jfxstnametv.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        jfxstlimittv.setCellValueFactory(new PropertyValueFactory<>("stockLimit"));
        jfxstcodetv.setCellValueFactory(new PropertyValueFactory<>("stockCode"));
        jfxstceiltv.setCellValueFactory(new PropertyValueFactory<>("ceilingPrice"));
        jfxstfloortv.setCellValueFactory(new PropertyValueFactory<>("floorPrice"));
        jfxstpricetv.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));
        jfxststatetv.setCellValueFactory(new PropertyValueFactory<>("stockState"));
        jfxstchangetv.setCellValueFactory(new PropertyValueFactory<>("stockChange"));

        stocktableview.setVisible(true);
        stocktableview.setEditable(false);
        stocktableview.setTableMenuButtonVisible(true);
        stocktableview.setItems(stockObservableList);
    }

    public void bindindex(){
        // TODO 指数数据绑定TableView
        jfxinnametv.setCellValueFactory(new PropertyValueFactory<>("indexName"));
        jfxincodetv.setCellValueFactory(new PropertyValueFactory<>("indexCode"));
        jfxinnumtv.setCellValueFactory(new PropertyValueFactory<>("indexPrice"));

        indextableview.setVisible(true);
        indextableview.setEditable(false);
        indextableview.setTableMenuButtonVisible(true);
        indextableview.setItems(indexObservableList);
    }

    public void clickintodetail(){
        // TODO 将选中股票的isSelect状态设置为选中
        //  单选、多选时先清空，再把选中的设置
        stocktableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<StockProperty> stockSelected=stocktableview.getSelectionModel().getSelectedItems();
        stocktableview.setOnMouseClicked(event -> {
            if (event.getClickCount()==2&&stockSelected.size()==1){
                try {
                    application.stage.close();
                    getApp().gotoStockDetailUI();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setstockstate(){
        // TODO 设置股票交易状态
        /*
        for (int i = 0; i < stockObservableList.size(); i++){
            if (!stockObservableList.get(i).isIsselect()){
                continue;
            }
            String newState=Choiceboxstate.getValue();
            stockObservableList.get(i).setStockState(newState);
            // TODO 在数据库中更改状态
            try {
                // TODO 连接
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_trading_system", "root","12345678");
                // TODO 到数据库中设置当前股票状态
                PreparedStatement pStmt = conn.prepareStatement("UPDATE stock_trading_system.stock SET stock_state=? WHERE stock_code=?");
                pStmt.setString(1,newState);
                pStmt.setString(2,stockObservableList.get(i).getStockCode());
                pStmt.executeUpdate();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e2){
                e2.printStackTrace();
            }
        }
         */

        // TODO 获取要修改的状态
        String newState=Choiceboxstate.getValue();
        String setState;
        if (newState.equals("暂停交易")){
            setState="stop";
        }else if(newState.equals("正常交易")){
            setState="restore";
        }else{
            setState="stop3";
        }

        List<StockProperty> stockSelected=stocktableview.getSelectionModel().getSelectedItems();

        // TODO 修改显示的信息
        for (int i=0;i<stockSelected.size();i++){
            for (int j=0;j<stockObservableList.size();j++){
                if (stockSelected.get(i).getStockCode().equals(stockObservableList.get(j).getStockCode())){
                    stockObservableList.get(j).setStockState(newState);
                    break;
                }
            }
        }

        // TODO 修改数据库中的信息
        List<Stock> stockList=new ArrayList<>();
        for (int i=0;i<stockSelected.size();i++){
            stockList.add(new Stock(stockSelected.get(i)));
        }
        String json = new Gson().toJson(stockList);
        CustomResp cr = new HttpCommon().doHttp("/stock/update_list/state/"+setState, "POST", json);

        System.out.println("设置交易状态成功");
    }

    public void setstocklimit() throws Exception{
        // TODO 设置股票涨跌幅
        double riseFallLimit;
        if (Choiceboxlimit.getValue().equals("5%")){
            riseFallLimit=0.05;
        }else if (Choiceboxlimit.getValue().equals("10%")){
            riseFallLimit=0.1;
        }else{
            riseFallLimit=-1;
        }
        /*
        for (int i = 0; i < stockObservableList.size(); i++) {
            if (!stockObservableList.get(i).isIsselect()){
                continue;
            }
            double highPrice,lowPrice;
            if (riseFallLimit<=0){
                // TODO 如果没有涨跌停限制，设置涨停价格为最大
                highPrice=-1;
                lowPrice=0;
            }else{
                highPrice=(1+riseFallLimit)*stockObservableList.get(i).getStockPrice();
                lowPrice=(1-riseFallLimit)*stockObservableList.get(i).getStockPrice();
            }
            stockObservableList.get(i).setCeilingPrice(highPrice);
            stockObservableList.get(i).setFloorPrice(lowPrice);
            stockObservableList.get(i).setStockLimit();
            // TODO 在数据库中更改最大涨跌幅，即涨停价格和跌停价格
            try {
                // TODO 连接
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_trading_system", "root","12345678");
                // TODO 到数据库中设置当前股票状态
                PreparedStatement pStmt = conn.prepareStatement("UPDATE stock_trading_system.stock SET ceiling_price=?, floor_price=? WHERE stock_code=?");
                pStmt.setDouble(1,highPrice);
                pStmt.setDouble(2,lowPrice);
                pStmt.setString(3,stockObservableList.get(i).getStockCode());
                pStmt.executeUpdate();
                conn.close();
            } catch (SQLException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e2){
                e2.printStackTrace();
            }
        }
         */

        List<StockProperty> stockSelected=stocktableview.getSelectionModel().getSelectedItems();

        // TODO 修改显示的信息
        for (int i=0;i<stockSelected.size();i++){
            for (int j=0;j<stockObservableList.size();j++){
                if (stockSelected.get(i).getStockCode().equals(stockObservableList.get(j).getStockCode())){
                    double highPrice,lowPrice;
                    if (riseFallLimit<=0){
                        // TODO 如果没有涨跌停限制，设置涨停价格为最大
                        highPrice=-1;
                        lowPrice=0;
                    }else{
                        highPrice=(1+riseFallLimit)*stockObservableList.get(j).getStockPrice();
                        lowPrice=(1-riseFallLimit)*stockObservableList.get(j).getStockPrice();
                    }
                    stockObservableList.get(j).setCeilingPrice(highPrice);
                    stockObservableList.get(j).setFloorPrice(lowPrice);
                    stockObservableList.get(j).setStockLimit();
                    break;
                }
            }
        }

        // TODO 修改数据库中的信息
        List<Stock> stockList=new ArrayList<>();
        for (int i=0;i<stockSelected.size();i++) {
            stockList.add(new Stock(stockSelected.get(i)));
        }
        String json = new Gson().toJson(stockList);
        double riseFallLimitTemp;
        if (riseFallLimit<=0){
            riseFallLimitTemp=-1;
        }else{
            riseFallLimitTemp=riseFallLimit*100;
        }
        CustomResp cr = new HttpCommon().doHttp("/stock/update_list/limit/"+riseFallLimitTemp, "POST", json);

        System.out.println("设置涨跌幅成功");
    }

    public void displaystock(){
        // TODO 连接数据库，并将stock信息放到arraylist中
        /*
        try {
            // TODO 连接
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_trading_system", "root","12345678");
            // TODO 到数据库中查询当前股票名称
            Statement stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM stock_trading_system.stock";
            ResultSet rs = stmt.executeQuery(sql);
            // TODO 放到st中
            while(rs.next()){
                Stock st=new Stock();
                st.setStockCode(rs.getString("stock_code"));
                st.setStockName(rs.getString("stock_name"));
                st.setStockPrice(Double.valueOf(rs.getString("stock_price")));
                st.setStockState(rs.getString("stock_state"));
                st.setCeilingPrice(Double.valueOf(rs.getString("ceiling_price")));
                st.setFloorPrice(Double.valueOf(rs.getString("floor_price")));
                st.setStockLimit();
                st.setStockChange();
                stockObservableList.add(st);
            }
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e2){
            e2.printStackTrace();
        }
         */

        CustomResp cr = new HttpCommon().doHttp("/stock/all", "GET", null);
        Type listType = new TypeToken<ArrayList<Stock>>(){}.getType();
        List<Stock> stocks = new Gson().fromJson(cr.getObjectJSON(), listType);
        for (int i = 0; i < stocks.size(); i++) {
            stockObservableList.add(new StockProperty(stocks.get(i)));
        }

        // TODO 已经放到缓存StockObservableList中，然后显示到表格里
        System.out.println("已经将股票数据导入缓存");
    }

    public void displayindex(){
        // TODO 连接数据库，并将index信息放到arraylist中，与stock类似
        /*
        try {
            // TODO 连接

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/stock_trading_system", "root","12345678");
            Statement stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM stock_trading_system";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Index in = new Index();
                in.setIndexCode(rs.getString("index_code"));
                in.setIndexName(rs.getString("index_name"));
                in.setIndexPrice(Double.valueOf(rs.getString("index_price")));
                indexObservableList.add(in);
            }
            conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e2){
            e2.printStackTrace();
        }
         */

        CustomResp cr = new HttpCommon().doHttp("/index/all", "GET", null);
        Type listType = new TypeToken<ArrayList<Index>>(){}.getType();
        List<Index> indexs = new Gson().fromJson(cr.getObjectJSON(), listType);
        for (int i = 0; i < indexs.size(); i++) {
            indexObservableList.add(new IndexProperty(indexs.get(i)));
        }

        // TODO 已经放到缓存IndexArraylist中，然后显示到表格里
        System.out.println("已经将指数数据导入到缓存");
    }
}
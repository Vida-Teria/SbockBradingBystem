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

/**
 * Class InterManageUIController extend from AdminUIController.
 * The setApp method is used to set application.
 * The getApp method is used to set application.
 * The {@code modifyPassword} method is used to go to modifyPasswordUI.
 * The {@code logout} method is used to logout the account and go back to AdminLoginUI.
 * The {@code quit} method is used to quit application.
 * The {@code initialize} method is used to initialize application.
 * The {@code setChoiceBox} method is used to set Choicebox and add choice into it.
 * The {@code bindStock} method is used to bind stock information with TableView.
 * The {@code bindIndex} method is used to bind index information with TableView.
 * The {@code clickIntoDetail} method is used to go to the StockDetailUIController.
 * The {@code setStockState} method is used to set stock state.
 * The {@code setStockLimit} method is used to set stock limit.
 * The {@code displayStock} method is used to get stock information from database.
 * The {@code displayIndex} method is used to get index information from database.
 */

public class InterManageUIController extends AdminUIController {
    private Main application;
    @FXML private ChoiceBox<String> choiceBoxLimit;
    @FXML private ChoiceBox<String> choiceBoxState;
    @FXML private TableView<StockProperty> stockTableView;
    @FXML private TableColumn<StockProperty,String> stockNameTableView;    //股票名称列
    @FXML private TableColumn<StockProperty,String> stockLimitTableView;    //股票涨跌幅限制列
    @FXML private TableColumn<StockProperty,String> stockCodeTableView;    //股票代码列
    @FXML private TableColumn<StockProperty,Double> stockCeilTableView;    //股票涨停价格列
    @FXML private TableColumn<StockProperty,Double> stockFloorTableView;    //股票跌停价格列
    @FXML private TableColumn<StockProperty,Double> stockPriceTableView;    //股票价格列
    @FXML private TableColumn<StockProperty,String> stockStateTableView;    //股票交易状态列
    @FXML private TableColumn<StockProperty,String> stockChangeTableView;    //股票涨跌幅（现）列
    @FXML private TableView<IndexProperty> indexTableView;
    @FXML private TableColumn<IndexProperty,String> indexNameTableView;    //指数名称列
    @FXML private TableColumn<IndexProperty,String> indexCodeTableView;    //指数代码列
    @FXML private TableColumn<IndexProperty,String> indexNumericTableView;    //指数数值列
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
        displayStock();
        bindStock();
        // TODO 显示指数信息，但现在还没有
        displayIndex();
        bindIndex();
        // TODO 下拉框初始化
        setChoiceBox();
        super.initialize(url, rb);
    }

    public void setChoiceBox(){
        choiceBoxState.getItems().add("正常交易");
        choiceBoxState.getItems().add("暂停交易");
        choiceBoxState.getItems().add("停牌三天");
        choiceBoxState.setValue("正常交易");
        choiceBoxLimit.getItems().add("5%");
        choiceBoxLimit.getItems().add("10%");
        choiceBoxLimit.getItems().add("无限制");
        choiceBoxLimit.setValue("10%");
    }

    public void bindStock(){
        // TODO 股票数据绑定TableView
        stockNameTableView.setCellValueFactory(new PropertyValueFactory<>("stockName"));
        stockLimitTableView.setCellValueFactory(new PropertyValueFactory<>("stockLimit"));
        stockCodeTableView.setCellValueFactory(new PropertyValueFactory<>("stockCode"));
        stockCeilTableView.setCellValueFactory(new PropertyValueFactory<>("ceilingPrice"));
        stockFloorTableView.setCellValueFactory(new PropertyValueFactory<>("floorPrice"));
        stockPriceTableView.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));
        stockStateTableView.setCellValueFactory(new PropertyValueFactory<>("stockState"));
        stockChangeTableView.setCellValueFactory(new PropertyValueFactory<>("stockChange"));

        stockTableView.setVisible(true);
        stockTableView.setEditable(false);
        stockTableView.setTableMenuButtonVisible(true);
        stockTableView.setItems(stockObservableList);
    }

    public void bindIndex(){
        // TODO 指数数据绑定TableView
        indexNameTableView.setCellValueFactory(new PropertyValueFactory<>("indexName"));
        indexCodeTableView.setCellValueFactory(new PropertyValueFactory<>("indexCode"));
        indexNumericTableView.setCellValueFactory(new PropertyValueFactory<>("indexPrice"));

        indexTableView.setVisible(true);
        indexTableView.setEditable(false);
        indexTableView.setTableMenuButtonVisible(true);
        indexTableView.setItems(indexObservableList);
    }

    public void clickIntoDetail(){
        // TODO 将选中股票的isSelect状态设置为选中
        //  单选、多选时先清空，再把选中的设置
        stockTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<StockProperty> stockSelected=stockTableView.getSelectionModel().getSelectedItems();
        stockTableView.setOnMouseClicked(event -> {
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

    public void setStockState() throws Exception{
        // TODO 设置股票交易状态
        
        // TODO 获取要修改的状态
        String newState=choiceBoxState.getValue();
        String setState;
        if (newState.equals("暂停交易")){
            setState="stop";
        }else if(newState.equals("正常交易")){
            setState="restore";
        }else{
            setState="stop3";
        }

        List<StockProperty> stockSelected=stockTableView.getSelectionModel().getSelectedItems();

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

        // TODO 跳转到提示界面
        if (cr.getResultJSON().substring(10,14).equals("true")){
            ControllerUtils.showAlert("[成功] 修改股票交易状态成功！");
        }else {
            ControllerUtils.showAlert("[失败] 修改股票交易状态失败！");
        }
        System.out.println("设置交易状态成功");
    }

    public void setStockLimit() throws Exception{
        // TODO 设置股票涨跌幅
        double riseFallLimit;
        if (choiceBoxLimit.getValue().equals("5%")){
            riseFallLimit=0.05;
        }else if (choiceBoxLimit.getValue().equals("10%")){
            riseFallLimit=0.1;
        }else{
            riseFallLimit=-1;
        }
        List<StockProperty> stockSelected=stockTableView.getSelectionModel().getSelectedItems();

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

        // TODO 跳转到提示界面
        if (cr.getResultJSON().substring(10,14).equals("true")){
            ControllerUtils.showAlert("[成功] 修改股票涨跌幅成功！");
        }else {
            ControllerUtils.showAlert("[失败] 修改股票涨跌幅失败！");
        }

        System.out.println("设置涨跌幅成功");
    }

    public void displayStock(){
        CustomResp cr = new HttpCommon().doHttp("/stock/all", "GET", null);
        Type listType = new TypeToken<ArrayList<Stock>>(){}.getType();
        List<Stock> stocks = new Gson().fromJson(cr.getObjectJSON(), listType);
        for (int i = 0; i < stocks.size(); i++) {
            stockObservableList.add(new StockProperty(stocks.get(i)));
        }

        // TODO 已经放到缓存StockObservableList中，然后显示到表格里
        System.out.println("已经将股票数据导入缓存");
    }

    public void displayIndex(){
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
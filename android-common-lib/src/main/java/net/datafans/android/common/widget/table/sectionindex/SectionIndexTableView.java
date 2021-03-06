package net.datafans.android.common.widget.table.sectionindex;

import android.content.Context;
import android.view.View;

import net.datafans.android.common.helper.FirstLetterHelper;
import net.datafans.android.common.widget.table.GroupTableView;
import net.datafans.android.common.widget.table.GroupTableViewDataSource;
import net.datafans.android.common.widget.table.TableView;
import net.datafans.android.common.widget.table.TableViewCell;
import net.datafans.android.common.widget.table.TableViewDataSource;
import net.datafans.android.common.widget.table.TableViewDelegate;
import net.datafans.android.common.widget.table.refresh.RefreshControlType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhonganyun on 15/8/18.
 */
public class SectionIndexTableView<T> implements GroupTableViewDataSource<T>, TableViewDelegate {


    //分组标题
    private Map<String, List<String>> titleMap = new HashMap<>();

    //首字母索引
    private Map<String, Map<Integer, Integer>> sectionMap = new HashMap<>();

    private List<String> sortedFirstCharList;



    private TableView tableView;


    private SectionIndexTableViewDataSource<T> dataSource;
    private SectionIndexTableViewDelegate delegate;

    protected SectionIndexTableView(Context context, RefreshControlType type,
                                 boolean enableRefresh, boolean enableLoadMore,
                                 boolean enableAutoLoadMore, SectionIndexTableViewDataSource<T> dataSource, SectionIndexTableViewDelegate delegate) {



        this.dataSource = dataSource;
        this.delegate = delegate;

        initFirstChars();

        GroupTableView.Builder<T> builder = new GroupTableView.Builder<>();
        builder.setContext(context);
        builder.setRefreshType(type);
        builder.setEnableRefresh(enableRefresh);
        builder.setEnableLoadMore(enableLoadMore);
        builder.setEnableAutoLoadMore(enableAutoLoadMore);
        builder.setDataSource(this);
        builder.setDelegate(this);
        tableView = builder.build();

    }

    public static class Builder<T>{

        private SectionIndexTableViewDataSource<T> dataSource;
        private SectionIndexTableViewDelegate delegate;
        private RefreshControlType refreshType = RefreshControlType.None;
        private Context context;
        private boolean enableRefresh = false;
        private boolean enableLoadMore = false;
        private boolean enableAutoLoadMore = false;



        public Builder<T> setDataSource(SectionIndexTableViewDataSource<T> dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder<T> setDelegate(SectionIndexTableViewDelegate delegate) {
            this.delegate = delegate;
            return this;
        }

        public Builder<T> setRefreshType(RefreshControlType refreshType) {
            this.refreshType = refreshType;
            return this;
        }

        public Builder<T> setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder<T> setEnableRefresh(boolean enableRefresh) {
            this.enableRefresh = enableRefresh;
            return this;
        }

        public Builder<T> setEnableLoadMore(boolean enableLoadMore) {
            this.enableLoadMore = enableLoadMore;
            return this;
        }

        public Builder<T> setEnableAutoLoadMore(boolean enableAutoLoadMore) {
            this.enableAutoLoadMore = enableAutoLoadMore;
            return this;
        }

        public SectionIndexTableView<T> build() {

            return new SectionIndexTableView<>(context, refreshType, enableRefresh, enableLoadMore, enableAutoLoadMore, dataSource, delegate);

        }
    }




    private void initFirstChars() {

        List<String> titles = dataSource.getIndexedTitles();

        for (int i = 0; i < titles.size(); i++) {

            String title = titles.get(i);
            String firstChar = FirstLetterHelper.getFirstLetter(title);

            List<String> sectionTitles = titleMap.get(firstChar);
            if (sectionTitles == null) {
                sectionTitles = new ArrayList<>();
                titleMap.put(firstChar, sectionTitles);
            }

            sectionTitles.add(title);

            Map<Integer, Integer> sectionTitleMap = sectionMap.get(firstChar);

            if (sectionTitleMap == null) {
                sectionTitleMap = new HashMap<>();
                sectionMap.put(firstChar, sectionTitleMap);
            }

            sectionTitleMap.put(sectionTitles.size() - 1, i);
        }

    }




    private List<String> getFirstCharList() {
        if (sortedFirstCharList == null) {
            sortedFirstCharList = new ArrayList<>();

            Set<String> charsSet = titleMap.keySet();
            for (String c : charsSet) {
                sortedFirstCharList.add(c);
            }

            Collections.sort(sortedFirstCharList, new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    return s1.compareTo(s2);
                }
            });
        }

        return sortedFirstCharList;
    }






    private boolean isShowUnIndexTitles() {
        return dataSource.getUnIndexedTitles() != null && dataSource.getUnIndexedTitles().size() > 0;
    }


    @Override
    public int getSections() {
        if (isShowUnIndexTitles()) return getFirstCharList().size() + 1;
        return getFirstCharList().size();
    }

    @Override
    public int getRows(int section) {
        if (isShowUnIndexTitles()) {

            if (section == 0) {
                return dataSource.getUnIndexedTitles().size();
            } else {
                String firstChar = getFirstCharList().get(section - 1);
                List<String> titles = titleMap.get(firstChar);
                return titles.size();
            }
        } else {
            String firstChar = getFirstCharList().get(section);
            List<String> titles = titleMap.get(firstChar);
            return titles.size();
        }
    }






    @Override
    public String getSectionHeaderTitle(int section) {
        if (isShowUnIndexTitles()) {
            if (section == 0) return "";
            else return getFirstCharList().get(section - 1);
        } else {
            return getFirstCharList().get(section);
        }
    }

    @Override
    public int getSectionHeaderHeight(int section) {
        if (isShowUnIndexTitles() && section == 0) return 1;
        return 100;
    }

    @Override
    public int getSectionFooterHeight(int section) {
        return 1;
    }


    @Override
    public String getSectionFooterTitle(int section) {
        return "";
    }

    @Override
    public TableViewCell<T> getTableViewCell(int section, int row) {
        return dataSource.getTableViewCell(section,row);
    }

    @Override
    public T getEntity(int section, int row) {
        if (isShowUnIndexTitles() && section == 0) {
            return dataSource.getUnIndexedEntity(row);

        } else {
            return dataSource.getEntity(getIndex(section, row));
        }
    }

    private Integer getIndex(int section, int row) {
        if (isShowUnIndexTitles()) section = section - 1;

        String firstChar = getFirstCharList().get(section);
        Map<Integer, Integer> map = sectionMap.get(firstChar);
        return map.get(row);
    }

    @Override
    public int getItemViewType(int section, int row) {
        return dataSource.getItemViewType(section, row);
    }

    @Override
    public int getItemViewTypeCount() {
        return dataSource.getItemViewTypeCount();
    }

    @Override
    public void onClickRow(int section, int row) {
        if (isShowUnIndexTitles() && section == 0) {
            delegate.onClickUnIndexedRow(row);

        } else {
            delegate.onClickRow(getIndex(section, row));
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }



    public View getView() {
        return tableView.getView();
    }
}

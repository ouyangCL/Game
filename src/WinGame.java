
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class WinGame{
    private static List<User> users = new ArrayList<>();

    private static List<Award> awardList = new ArrayList<>();

    private static List<Appear> appearHistoryList = new ArrayList<>();

    private static Map<String,UserOperate> userOperateMap = new HashMap();

    private static User currentUser;

    private static Boolean isOpen = false;
    static {
        Award award = new Award();
        award.setAwardId(1);
        award.setName("小麦");
        award.setRate(BigDecimal.valueOf(2));
        award.setLargeRate(BigDecimal.valueOf(7));
        awardList.add(award);

        Award award2 = new Award();
        award2.setAwardId(2);
        award2.setName("大豆");
        award2.setRate(BigDecimal.valueOf(2));
        award2.setLargeRate(BigDecimal.valueOf(8));
        awardList.add(award2);

        Award award3 = new Award();
        award3.setAwardId(3);
        award3.setName("花生");
        award3.setRate(BigDecimal.valueOf(2));
        award3.setLargeRate(BigDecimal.valueOf(9));
        awardList.add(award3);

        Award award4 = new Award();
        award4.setAwardId(4);
        award4.setName("玉米");
        award4.setRate(BigDecimal.valueOf(2));
        award4.setLargeRate(BigDecimal.valueOf(5));
        awardList.add(award4);

        Award award5 = new Award();
        award5.setAwardId(5);
        award5.setName("丝绸");
        award5.setRate(BigDecimal.valueOf(2));
        award5.setLargeRate(BigDecimal.valueOf(20));
        awardList.add(award5);

        Award award6 = new Award();
        award6.setAwardId(6);
        award6.setName("黄金");
        award6.setRate(BigDecimal.valueOf(2));
        award6.setLargeRate(BigDecimal.valueOf(20));
        awardList.add(award6);

        Award award7 = new Award();
        award7.setAwardId(7);
        award7.setName("白银");
        award7.setRate(BigDecimal.valueOf(2));
        award7.setLargeRate(BigDecimal.valueOf(20));
        awardList.add(award7);

        Award award8 = new Award();
        award8.setAwardId(8);
        award8.setName("钻石");
        award8.setRate(BigDecimal.valueOf(2));
        award8.setLargeRate(BigDecimal.valueOf(40));
        awardList.add(award8);
    }
    private static void start(){
        while (true){
            Scanner s = new Scanner(System.in);
            if(currentUser == null) {
                System.out.println("1:注册 2:登陆");
                String menu = s.next();
                while (!Arrays.asList("1","2").contains(menu)){
                    System.out.println("1:注册 2:登陆");
                    menu = s.next();
                }
                switch (menu){
                    case "1":
                        regist();
                        break;
                    case "2":
                        login();
                        break;
                    default:
                        break;
                }
            }
            if(currentUser == null){
                continue;
            }
            System.out.println("1: 下注 2: 查看余额 3: 查看下注记录 4: 查看出奖历史 5: 充值 6: 退出登陆");
            String menu = s.next();
            while (!Arrays.asList("1","2","3","4","5","6").contains(menu)){
                System.out.println("1: 下注 2: 查看余额 3: 查看下注记录 4: 查看出奖历史 5: 充值 6: 退出登陆");
                menu = s.next();
            }
            switch (menu){
                case "1":
                    if(isOpen){
                        System.out.println("开奖不能下注");
                        break;
                    }
                    UserOperate userOperate = userOperateMap.get(currentUser.getUserId());
                    List<Operate> operateList = new ArrayList<>();
                    if(userOperate == null){
                        userOperate = new UserOperate();
                        userOperate.setUserId(currentUser.getUserId());
                    }else{
                        operateList = userOperate.getOperateList();
                    }
                    Operate operate = new Operate();
                    System.out.println("-----请下注-----");
                    awardList.forEach(award -> {
                        System.out.println(award.getAwardId()+":"+award.getName());
                    });
                    System.out.println("请输入下注编号：");
                    s = new Scanner(System.in);
                    int awardId = Integer.parseInt(s.next());
                    Set<Integer> awardIds = awardList.stream().map(Award::getAwardId).collect(Collectors.toSet());
                    while (!awardIds.contains(awardId)){
                        System.out.println("请输入正确编号：");
                        awardId = Integer.parseInt(s.next());
                    }
                    operate.setAwardId(awardId);
                    System.out.println("请输入下注金额：");
                    s = new Scanner(System.in);
                    BigDecimal pour = BigDecimal.valueOf(Double.parseDouble(s.next()));
                    operate.setPour(pour);
                    boolean hasMoney = true;
                    for (User user : users) {
                        if (user.getUserId().equals(currentUser.getUserId())) {
                            if (user.getUserCount().compareTo(pour) > -1) {
                                user.setUserCount(user.getUserCount().subtract(pour));
                            } else {
                                System.out.println("余额不足！请充值....");
                                hasMoney = false;
                                break;
                            }
                        }
                    }
                    if(!hasMoney){
                        break;
                    }
                    operateList.add(operate);
                    userOperate.setOperateList(operateList);
                    operateList.forEach(operate1 -> {
                        operate1.setAwardName(awardList.stream().filter(award -> award.getAwardId().equals(operate1.getAwardId())).findAny().get().getName());
                    });
                    userOperateMap.put(currentUser.getUserId(),userOperate);
                    System.out.println("下注成功!!");
                    System.out.println("------下注情况-----");
                    operateList.forEach(operate1 -> {
                        System.out.println(operate1.getAwardName()+": "+operate1.getPour());
                    });
                    break;
                case "2":
                    System.out.println("余额："+currentUser.getUserCount());
                    break;
                case "3":
                    UserOperate userOperate1 = userOperateMap.get(currentUser.getUserId());
                    if(userOperate1 == null){
                        System.out.println("暂无下注....");
                    }else {
                        userOperate1.getOperateList().forEach(operate1 -> {
                            System.out.println(operate1.getAwardName() + ": " + operate1.getPour());
                        });
                    }
                    break;
                case "4":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    appearHistoryList.forEach(appear -> {
                        System.out.println(sdf.format(appear.getAppearDate())+": "+appear.getAward().getName()+(appear.getType() == 1 ? "风靡" : "流行"));
                    });
                    break;
                case "5":
                    System.out.println("请输入充值金额：");
                    s = new Scanner(System.in);
                    Scanner finalS = s;
                    users.forEach(user -> {
                        if(currentUser.getUserId().equals(user.getUserId())){
                            user.setUserCount(user.getUserCount().add(BigDecimal.valueOf(Double.valueOf(finalS.next()))));
                        }
                    });
                    System.out.println("充值成功，余额："+getUser(currentUser.getName()).getUserCount());
                    break;
                case "6":
                    logout();
                    break;
                default:
                    break;
            }
        }
    }
    private static User getUser(String name){
        Optional<User> userOptional = users.stream().filter(user -> user.getName().equals(name)).findFirst();
        if(userOptional.isPresent()){
            return userOptional.get();
        }else {
            System.out.println("用户不存在");
            return null;
        }
    }

    private static void open(){
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isOpen = true;
                System.out.println("-----准备开奖 暂停下注-----");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Appear appear = new Appear();
                appear.setAppearId(UUID.randomUUID().toString());

                Random random = new Random();
                Award award = awardList.get(random.nextInt(awardList.size()));
                appear.setAward(award);
                appear.setAppearDate(new Date());
                appear.setType(random.nextInt(2));
                appearHistoryList.add(appear);

                BigDecimal rate;
                if(appear.getType() == 0){
                    rate = award.getRate();
                }else{
                    rate = award.getLargeRate();
                }

                BigDecimal finalRate = rate;
                Map<String, BigDecimal> winningMap = new HashMap<>();
                userOperateMap.forEach((k,v) -> v.getOperateList().forEach(operate -> {
                    if (operate.getAwardId().equals(award.getAwardId())) {
                        BigDecimal sum = operate.getPour().multiply(finalRate);
                        users.forEach(user -> {
                            if(user.getUserId().equals(k)){
                                BigDecimal win = BigDecimal.valueOf(0);
                                if(winningMap.containsKey(user.getName())){
                                    win = winningMap.get(user.getName());
                                }
                                user.setUserCount(user.getUserCount().add(sum));
                                winningMap.put(user.getName(),win.add(sum));
                            }
                        });
                    }
                }));
                userOperateMap = new HashMap<>();
                System.out.println("-----出奖结果-----");
                System.out.println(award.getName()+(appear.getType()==1?"风靡啦":"流行"));
                System.out.println("-----中奖名单-----");
                if(winningMap.size() == 0){
                    System.out.println("无人中奖～～～～");
                }else {
                    winningMap.forEach((k, v) -> {
                        System.out.println(k + ": " + v);
                    });
                }
                System.out.println("--------用户余额--------");
                users.forEach(user -> {
                    System.out.println(user.getName()+": "+user.getUserCount());
                });
                System.out.println("-----------------------");
                isOpen = false;

            }
        }).start();
    }
    private static void regist(){
        System.out.println("注册用户...");
        Scanner s = new Scanner(System.in);
        User user = new User();
        System.out.println("设置用户名：");
        user.setName(s.next());
        user.setUserCount(BigDecimal.valueOf(0.0));
        user.setUserId(UUID.randomUUID().toString());
        users.add(user);
    }

    private static void login(){
        Scanner s = new Scanner(System.in);
        System.out.println("用户名：");
        String name = s.next();
        currentUser = getUser(name);
    }

    private static void logout(){
        currentUser = null;
    }


    public static void main(String[] args) {
        open();
        start();
    }
}
class User{
    private String userId;
    private String name;
    private BigDecimal userCount;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUserCount() {
        return userCount;
    }

    public void setUserCount(BigDecimal userCount) {
        this.userCount = userCount;
    }
}
class Award{
    private Integer awardId;
    private String name;
    private BigDecimal rate;
    private BigDecimal largeRate;

    public Integer getAwardId() {
        return awardId;
    }

    public void setAwardId(Integer awardId) {
        this.awardId = awardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getLargeRate() {
        return largeRate;
    }

    public void setLargeRate(BigDecimal largeRate) {
        this.largeRate = largeRate;
    }
}
class UserOperate{
    private String userId;
    private List<Operate> operateList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Operate> getOperateList() {
        return operateList;
    }

    public void setOperateList(List<Operate> operateList) {
        this.operateList = operateList;
    }
}
class Operate {
    private Integer awardId;
    private BigDecimal pour;
    private String awardName;

    public Integer getAwardId() {
        return awardId;
    }

    public void setAwardId(Integer awardId) {
        this.awardId = awardId;
    }

    public BigDecimal getPour() {
        return pour;
    }

    public void setPour(BigDecimal pour) {
        this.pour = pour;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }
}
class Appear {
    private String appearId;
    private Award award;
    private Integer Type; // 0流行 1风靡
    private Date appearDate;

    public String getAppearId() {
        return appearId;
    }

    public void setAppearId(String appearId) {
        this.appearId = appearId;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public Date getAppearDate() {
        return appearDate;
    }

    public void setAppearDate(Date appearDate) {
        this.appearDate = appearDate;
    }
}
package com.example.androidapp.LinkGame.LinkModel;

//import static com.example.linkgame_beta.LinkGame.Constant.FinalValue.UNBLOCKED;

public class Kernel {

    public static final int UNBLOCKED =-1;
    private static boolean directLink(int[][] LinkBoard, Point src, Point des, SealLinkInfo SealedLinkInfo) {

        if (src.getX() == des.getX()) {
            int min = Math.min(src.getY(), des.getY());
            int max = Math.max(src.getY(), des.getY());
            for (int i = min+1; i < max; i++) {
                if (LinkBoard[src.getX()][i] != UNBLOCKED)
                    return false;
            }
            if (SealedLinkInfo != null)
                SealedLinkInfo.Seal(new LinkInfo(src, des));
            return true;
        } else if (src.getY() == des.getY()) {
            int min = Math.min(src.getX(), des.getX());
            int max = Math.max(src.getX(), des.getX());
            for (int i = min+1; i < max; i++) {
                if (LinkBoard[i][src.getY()] != UNBLOCKED)
                    return false;
            }
            if (SealedLinkInfo != null)
                SealedLinkInfo.Seal(new LinkInfo(src, des));
            return true;
        }
        return false;
    }

    private static boolean oneBendLink(int[][] LinkBoard, Point src, Point des, SealLinkInfo SealedLinkInfo) {
        Point tmp1 = new Point(des.getX(), src.getY());
        Point tmp2 = new Point(src.getX(), des.getY());
        boolean judge1 = directLink(LinkBoard, src, tmp1, null)
                && directLink(LinkBoard, tmp1, des, null);
        boolean judge2 = directLink(LinkBoard, src, tmp2, null)
                && directLink(LinkBoard, tmp2, des, null);
        if (judge1 && LinkBoard[tmp1.getX()][tmp1.getY()] == UNBLOCKED) {
            if (SealedLinkInfo != null) {
                SealedLinkInfo.Seal(new LinkInfo(src, tmp1, des));
            }
            return true;
        }
        if (judge2 && LinkBoard[tmp2.getX()][tmp2.getY()] == UNBLOCKED) {
            if (SealedLinkInfo != null) {
                SealedLinkInfo.Seal(new LinkInfo(src, tmp2, des));
            }
            return true;
        }
        return false;
    }

    private static boolean twoBendsLink(int[][] LinkBoard, Point src, Point des, SealLinkInfo SealedLinkInfo) {
        int row = LinkBoard.length;
        int col = LinkBoard[0].length;
//        boolean link
        for (int i = 0; i < col; i++)//垂直方向
        {
            Point tmp1 = new Point(src.getX(), i);
            Point tmp2 = new Point(des.getX(), i);
            if (isLinkedAndUnblocked(LinkBoard, src, des, tmp1, tmp2)) {
                if (SealedLinkInfo != null) {
                    SealedLinkInfo.Seal(new LinkInfo(src, tmp1, tmp2, des));
                }
                return true;
            };
        }
        for (int j = 0; j < row ; j++)//水平方向
        {
            Point tmp1 = new Point(j, src.getY());
            Point tmp2 = new Point(j, des.getY());
            if (isLinkedAndUnblocked(LinkBoard, src, des,tmp1, tmp2)) {
                if (SealedLinkInfo != null) {
                    SealedLinkInfo.Seal(new LinkInfo(src, tmp1, tmp2, des));
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isLinkedAndUnblocked(int[][] LinkBoard, Point src, Point des, Point tmp1, Point tmp2) {
        boolean isLinked = directLink(LinkBoard, src, tmp1, null)
                && directLink(LinkBoard, tmp1, tmp2, null)
                && directLink(LinkBoard, tmp2, des, null);
        boolean isUnblocked = LinkBoard[tmp1.getX()][tmp1.getY()] == UNBLOCKED
                && LinkBoard[tmp2.getX()][tmp2.getY()] == UNBLOCKED;
        return isLinked&&isUnblocked;
    }

    public static boolean findLink(int[][] LinkBoard, Point src, Point des, SealLinkInfo sealLinkInfo)
    {
        int x_s = src.getX();int y_s = src.getY();
        int x_d = des.getX();int y_d = des.getY();
        boolean Same = LinkBoard[x_s][y_s]==LinkBoard[x_d][y_d];
        boolean NUnblocked= !(LinkBoard[x_s][y_s]==UNBLOCKED ||LinkBoard[x_d][y_d]==UNBLOCKED);
        if(Same&&NUnblocked)
        {
            return directLink(LinkBoard,src,des,sealLinkInfo)||
                    oneBendLink(LinkBoard,src,des,sealLinkInfo)||
                    twoBendsLink(LinkBoard,src,des,sealLinkInfo);
        }
        return false;
    }
}

//    private static boolean isValueEqual(int[][] LinkBoard,Coordinate A,Coordinate B)
//    {
//        boolean a=LinkBoard[A.getX()][A.getY()]==UNBLOCKED||LinkBoard[B.getX()][B.getY()]==UNBLOCKED;
//        boolean b=LinkBoard[A.getX()][A.getY()]==LinkBoard[B.getX()][B.getY()];
//        return a&&b;
//    }



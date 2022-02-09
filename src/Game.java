import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {

    //2 - FocusBlock
    //1 - Celula
    //11- Celula para nascer
    //91- Celula para morrer
    //
    private final int[] focusBlock = {0, 0};
    private final int cellsForStartGame = 3;
    private int blocksX = 9;
    private int blocksY = 9;
    private int[][] gameArr = new int[blocksX][blocksY];
    private int blocksSize = 10;
    private final int msOfTimer = 150;//in ms

    private final int shadowVariant = -35;

    public Game() {
        setFocusable(true);
        MouseAdapter mouseInput = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    int x = e.getX() / blocksSize;
                    int y = e.getY() / blocksSize;
                    if (gameArr[x][y] == 0 || gameArr[x][y] == 2) {
                        gameArr[x][y] = 1;
                        repaint();
                    } else if (gameArr[x][y] == 1) {
                        gameArr[x][y] = 0;
                        repaint();
                    }
                } catch (Exception er) {
                }

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                try {
                    int x = e.getX() / blocksSize;
                    int y = e.getY() / blocksSize;
                    if (!(x == focusBlock[0] && y == focusBlock[1])) {
                        for (int x2 = 0; x2 < blocksX; x2++) {
                            for (int y2 = 0; y2 < blocksY; y2++) {
                                if (gameArr[x2][y2] == 2)
                                    gameArr[x2][y2] = 0;
                            }
                        }
                        focusBlock[0] = x;
                        focusBlock[1] = y;
                        if (gameArr[x][y] == 0) {
                            gameArr[x][y] = 2;
                            repaint();
                        }
                    }
                } catch (Exception exception) {
                }
            }
        };
        addMouseMotionListener(mouseInput);
        addMouseListener(mouseInput);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                ReSizeGame();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        ReSizeGame();
        setVisible(true);
        Timer timer = new Timer(msOfTimer, this);
        timer.start();
    }

    //Game Execute
    @Override
    public void actionPerformed(ActionEvent e) {
        if (moreThanCell(cellsForStartGame)) UpdateGame();
        if (moreThanCell(1)) ZoomIn();
        repaint();
    }

    private void UpdateGame() {
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                if (gameArr[x][y] == 1 || gameArr[x][y] == 91)
                    cellValidate(x, y);
            }
        }
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                if (gameArr[x][y] == 11) gameArr[x][y] = 1;
                else if (gameArr[x][y] == 91) gameArr[x][y] = 0;
            }
        }
    }

    private void ZoomIn() {
        int rx = 0, lx = blocksX;
        int by = 0, ty = blocksX;

        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                if (gameArr[x][y] == 1) {
                    if (rx < x) rx = x;
                    if (lx > x) lx = x;
                    if (by < y) by = y;
                    if (ty > y) ty = y;
                }
            }
        }
        int newBlockX = rx - lx + 3, newBlockY = by - ty + 3;
        int[][] newGameArr = new int[newBlockX][newBlockY];
        for (int x = 1, x2 = lx; x < newBlockX && x2 <= rx; x++, x2++) {
            for (int y = 1, y2 = ty; y < newBlockY && y2 <= by; y++, y2++) {
                newGameArr[x][y] = gameArr[x2][y2];
            }
        }
        gameArr = newGameArr;
        blocksX = newBlockX;
        blocksY = newBlockY;
        ReSizeGame();
    }

    private void ReSizeGame() {
        int height = getHeight();
        int width = getWidth();
        int blocksWidth = width / blocksX;
        int blocksHeight = height / blocksY;

        blocksSize = Math.min(blocksHeight, blocksWidth);
        repaint();
    }

    private void cellValidate(int x, int y) {
        int cells = 0;
        for (int x2 = Math.max(x - 1, 0); x2 < Math.min(x + 2, blocksX); x2++) {
            for (int y2 = Math.max(y - 1, 0); y2 < (Math.min(y + 2, blocksY)); y2++) {
                if (!(x2 == x && y2 == y)) {
                    if (gameArr[x2][y2] == 1 || gameArr[x2][y2] == 91)
                        cells++;
                    else if ((gameArr[x2][y2] == 0 || gameArr[x2][y2] == 2) && (gameArr[x][y] == 1 || gameArr[x][y] == 91))
                        cellValidate(x2, y2);
                }
            }
        }

        //Todo Talvez tenha um erro nesse != substituir por gameArr[x][y]==0 || gameArr[x][y]==2
        if (gameArr[x][y] != 1 && cells == 3)
            gameArr[x][y] = 11;
        if (gameArr[x][y] == 1 && (cells < 2 || cells > 3))
            gameArr[x][y] = 91;
    }

    private boolean moreThanCell(int numberOfCells) {
        int cells = 0;
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                if (gameArr[x][y] == 1)
                    cells++;
                if (cells >= numberOfCells)
                    return true;
            }
        }
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    //Draw game
    private int validateRGB(int a) {
        if (a > 255)
            return 255;
        return Math.max(a, 0);
    }

    private Color colorVariant(Color color, int variant) {
        int[] newColor = new int[3];
        newColor[0] = validateRGB(color.getRed() + variant);
        newColor[1] = validateRGB(color.getGreen() + variant);
        newColor[2] = validateRGB(color.getBlue() + variant);
        return new Color(newColor[0], newColor[1], newColor[2]);
    }

    private void drawBlock(int x, int y, Color color, Graphics g) {
        g.setColor(color);
        g.fillRect(x * blocksSize, y * blocksSize, blocksSize, blocksSize);
        int borderSize = Math.max((int) Math.floor(blocksSize / 100.0 * 10), 1);
        if (blocksSize > 2) {
            g.setColor(colorVariant(color, shadowVariant));
            g.fillRect(x * blocksSize, y * blocksSize, blocksSize, borderSize);//Top
            g.fillRect(x * blocksSize + blocksSize - borderSize, y * blocksSize, borderSize, blocksSize);// Bottom
            g.fillRect(x * blocksSize, y * blocksSize, borderSize, blocksSize);//Left
            g.fillRect(x * blocksSize, y * blocksSize + blocksSize - borderSize, blocksSize, borderSize);// Right
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, 10000, 10000);
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                if (gameArr[x][y] == 1) {
                    drawBlock(x, y, Color.orange, g);
                } else if (gameArr[x][y] == 2) {
                    drawBlock(x, y, Color.gray, g);
                } else {
                    drawBlock(x, y, Color.darkGray, g);
                }
            }
        }
    }

}

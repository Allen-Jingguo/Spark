import { PageContainer } from '@ant-design/pro-components';
import {
  Space,
  Table,
  Tag,
  Button,
  Col,
  Form,
  Input,
  Row,
  Select,
  UpOutlined,
  DownOutlined,
  message,
  Popconfirm,
} from 'antd';
import {
  ProFormDatePicker,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
  QueryFilter,
} from '@ant-design/pro-components';
import { Card } from 'antd';
import React, { useState, useEffect } from 'react';
import outboundApi from '@/services/spark/outboundApi';
import OutboundCreateModal from '@/pages/Outbound/outboundCreateModal';


const view = (record) => {
  outboundApi.view({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('start success!');
    }
  });
  message.success('operate successfully');
};

const disable = (record) => {
  console.log(record);
  outboundApi.disable({ ...record }).then((result) => {
    console.log(result);
    if (result) {
      message.success('operate successfully!');
    }
  });
  message.success('operate successfully');
};

const cancel = (e) => {
  console.log(e);
  message.success('Canceled');
};

const columns = [
  // {
  //   title: 'Id',
  //   dataIndex: 'id',
  //   disable: true,
  // },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text}</a>,
  },

  {
    title: 'Type',
    dataIndex: 'type',
  },

  {
    title: 'HostName',
    dataIndex: 'hostName',
  },

  {
    title: 'User Name',
    dataIndex: 'userName',
  },

  {
    title: 'Password',
    dataIndex: 'password',
  },

  {
    title: 'Created Time',
    dataIndex: 'createdTime',
  },

  {
    title: 'Action',
    key: 'action',
    render: (_, record) => (
      <Space size="middle">
        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => view(record)}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <a href="#" style={{ fontSize: 15 }}>
            View
          </a>
        </Popconfirm>

        <Popconfirm
          title="Are you sure to start this flow?"
          onConfirm={() => disable(record)}
          onCancel={cancel}
          okText="Yes"
          cancelText="No"
        >
          <a href="#" style={{ fontSize: 15 }}>
            disable
          </a>
        </Popconfirm>
      </Space>
    ),
  },
];

const data = [
  {
    key: '1',
    id: '1',
    name: 'Ord  Extract Static Data Flow',
    inboundName: 'Ord JDBC Inbound Name',
    parserName: 'JDBCParser',
    filterName: 'NA',
    keyMapperName: 'KeyValueMapper',
    formatterName: 'JDBCFormatter',
    outboundName: 'WSS JDBC Oubound Name',
  },
  {
    key: '2',
    id: '2',
    name: 'SDR Static Data Flow',
    inboundName: 'SDR JDBC Inbound Name',
    parserName: 'JDBCParser',
    filterName: 'NA',
    keyMapperName: 'KeyValueMapper',
    formatterName: 'NA',
    outboundName: 'Ord JDBC Oubound Name',
  },
];

const OutboundList = () => {
  const [form] = Form.useForm();
  const [tableList, setTableList] = useState(data);
  const [showCreateModal, setShowCreateModal] = useState(false);

  useEffect(() => {}, []);

  const doSearch = (values) => {
    outboundApi.getList(values).then((datas) => {
      setTableList(flowList);
    });
  };

  const closeCreateModal = () => {
    setShowCreateModal(false);
  };

  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            'radial-gradient(circle at 97% 10%, #EBF2FF 0%, #F5F8FF 28%, #EBF1FF 124%)',
        }}
      >
        <div>
          <Form
            form={form}
            name="advanced_search"
            className="ant-advanced-search-form"
            onFinish={doSearch}
          >
            <Row gutter={24}>
              <Col>
                <Form.Item
                  name="name"
                  label="Name"
                  rules={[{ required: true, message: 'Missing name' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col>
                <Form.Item name="type" label="Type">
                  <Select
                    options={[
                      { label: 'Default', value: 'default' },
                      { label: 'Transation', value: 'Transation' },
                    ]}
                    style={{ width: 150 }}
                  />
                </Form.Item>
              </Col>

              <Col
                //span={24}
                style={{
                  textAlign: 'left',
                }}
              >
                <Button type="primary" htmlType="submit">
                  Search
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    form.resetFields();
                  }}
                >
                  Clear
                </Button>
                <Button
                  style={{ marginLeft: 10 }}
                  type="primary"
                  onClick={() => {
                    setShowCreateModal(true);
                  }}
                >
                  Create New
                </Button>
              </Col>
            </Row>
          </Form>
        </div>
        <div style={{ marginTop: 20 }}>
          <Table columns={columns} dataSource={tableList} pagination={true} />
        </div>
      </Card>
      {showCreateModal && <OutboundCreateModal closeCreateModal={closeCreateModal} />}
    </PageContainer>
  );
};
export default OutboundList;
